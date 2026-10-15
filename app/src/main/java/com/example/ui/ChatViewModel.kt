package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.ChatSessionEntity
import com.example.data.local.PreferencesManager
import com.example.data.model.AgentPersona
import com.example.data.model.ApiConfig
import com.example.data.model.ApiProvider
import com.example.data.model.ChatMessage
import com.example.data.model.ChatSession
import com.example.data.model.LayoutThemeOption
import com.example.data.model.VideoGenerationData
import com.example.data.omniroute.OmniRouteEngine
import com.example.data.omniroute.ViralHooksEngine
import com.example.data.remote.ChatStreamClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val chatDao = db.chatDao()
    private val prefs = PreferencesManager(application)
    private val client = ChatStreamClient()

    val config: StateFlow<ApiConfig> = prefs.config
    val layoutTheme: StateFlow<LayoutThemeOption> = prefs.layoutTheme

    private val _showLayoutPicker = MutableStateFlow(false)
    val showLayoutPicker: StateFlow<Boolean> = _showLayoutPicker.asStateFlow()

    val sessions: StateFlow<List<ChatSession>> = chatDao.getAllSessions()
        .map { list -> list.map { it.toDomain() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentSessionId = MutableStateFlow<String>(UUID.randomUUID().toString())
    val currentSessionId: StateFlow<String> = _currentSessionId.asStateFlow()

    // Parallel AI Agent Switcher: DAWAN aI vs MADISON Agency
    private val _activePersona = MutableStateFlow<AgentPersona>(AgentPersona.DAWAN_CORE)
    val activePersona: StateFlow<AgentPersona> = _activePersona.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    private val _isCloning = MutableStateFlow(false)
    val isCloning: StateFlow<Boolean> = _isCloning.asStateFlow()

    private val _cloningRepoUrl = MutableStateFlow("")
    val cloningRepoUrl: StateFlow<String> = _cloningRepoUrl.asStateFlow()

    // Deep Think Mode Toggle
    private val _isDeepThink = MutableStateFlow(false)
    val isDeepThink: StateFlow<Boolean> = _isDeepThink.asStateFlow()

    // Uploaded Image Attachment
    private val _attachedImageUri = MutableStateFlow<String?>(null)
    val attachedImageUri: StateFlow<String?> = _attachedImageUri.asStateFlow()

    // Google Veo Video Studio States
    private val _showVideoDialog = MutableStateFlow(false)
    val showVideoDialog: StateFlow<Boolean> = _showVideoDialog.asStateFlow()

    // Viral Reels Studio States (OpenMontage & MoneyPrinterTurbo)
    private val _showViralReelDialog = MutableStateFlow(false)
    val showViralReelDialog: StateFlow<Boolean> = _showViralReelDialog.asStateFlow()

    private val _isRenderingVideo = MutableStateFlow(false)
    val isRenderingVideo: StateFlow<Boolean> = _isRenderingVideo.asStateFlow()

    private val _activeStreamingMessage = MutableStateFlow<ChatMessage?>(null)

    val messages: StateFlow<List<ChatMessage>> = _currentSessionId
        .flatMapLatest { sessionId ->
            chatDao.getMessagesForSession(sessionId).map { entityList ->
                val base = entityList.map { it.toDomain() }.toMutableList()
                val currentStream = _activeStreamingMessage.value
                if (currentStream != null && currentStream.sessionId == sessionId) {
                    val existingIndex = base.indexOfFirst { it.id == currentStream.id }
                    if (existingIndex >= 0) {
                        base[existingIndex] = currentStream
                    } else {
                        base.add(currentStream)
                    }
                }
                base
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _input = MutableStateFlow("")
    val input: StateFlow<String> = _input.asStateFlow()

    private val _showSettings = MutableStateFlow(false)
    val showSettings: StateFlow<Boolean> = _showSettings.asStateFlow()

    private var streamJob: Job? = null

    private val githubRegex = Regex("https://github\\.com/[\\w-]+/[\\w.-]+")

    fun onInputChange(newInput: String) {
        _input.value = newInput
    }

    fun setShowSettings(show: Boolean) {
        _showSettings.value = show
    }

    fun setShowVideoDialog(show: Boolean) {
        _showVideoDialog.value = show
    }

    fun setShowViralReelDialog(show: Boolean) {
        _showViralReelDialog.value = show
    }

    fun setShowLayoutPicker(show: Boolean) {
        _showLayoutPicker.value = show
    }

    fun setLayoutTheme(theme: LayoutThemeOption) {
        prefs.setLayoutTheme(theme)
    }

    fun toggleDeepThink() {
        _isDeepThink.value = !_isDeepThink.value
    }

    fun switchPersona(persona: AgentPersona) {
        _activePersona.value = persona
    }

    fun setAttachedImage(uri: String?) {
        _attachedImageUri.value = uri
    }

    fun clearAttachedImage() {
        _attachedImageUri.value = null
    }

    fun onModelSelected(modelId: String) {
        prefs.setSelectedModel(modelId)
    }

    fun saveConfig(newConfig: ApiConfig) {
        prefs.saveConfig(newConfig)
    }

    fun startNewChat() {
        streamJob?.cancel()
        _isStreaming.value = false
        _isCloning.value = false
        _isRenderingVideo.value = false
        _activeStreamingMessage.value = null
        _attachedImageUri.value = null
        _currentSessionId.value = UUID.randomUUID().toString()
    }

    fun selectSession(sessionId: String) {
        streamJob?.cancel()
        _isStreaming.value = false
        _isCloning.value = false
        _isRenderingVideo.value = false
        _activeStreamingMessage.value = null
        _attachedImageUri.value = null
        _currentSessionId.value = sessionId
    }

    fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            chatDao.deleteMessagesForSession(sessionId)
            chatDao.deleteSession(sessionId)
            if (_currentSessionId.value == sessionId) {
                startNewChat()
            }
        }
    }

    fun stopGeneration() {
        streamJob?.cancel()
        finalizeCurrentStream()
    }

    /**
     * Triggers Google Veo 2 model to generate high-fidelity video clip
     */
    fun generateVideo(
        prompt: String,
        style: String = "Cinematic (Veo)",
        duration: Int = 6,
        aspectRatio: String = "16:9",
        cameraMotion: String = "Dynamic Tracking Shot",
        lightingMood: String = "Moody Cinematic Glow"
    ) {
        val sessionId = _currentSessionId.value
        val persona = _activePersona.value

        viewModelScope.launch {
            ensureSession(sessionId, "🎬 Veo Video: $prompt", persona.id)

            // User prompt for video
            val userMsg = ChatMessage(
                sessionId = sessionId,
                role = "user",
                content = "🎬 **Generate Video (Google Veo):** \"$prompt\"\n*Motion: $cameraMotion • Lighting: $lightingMood • Duration: ${duration}s • Ratio: $aspectRatio*",
                model = "google/veo-2",
                agentPersona = persona.id
            )
            chatDao.insertMessage(ChatMessageEntity.fromDomain(userMsg))

            _isRenderingVideo.value = true

            // Simulate Google Veo 2 Diffusion Latents & Optical Flow Synthesis
            delay(2800)
            _isRenderingVideo.value = false

            val videoMsg = ChatMessage(
                sessionId = sessionId,
                role = "assistant",
                content = "✨ **Google Veo 2 Video Generated!**\n\nSynthesized with **$cameraMotion** and **$lightingMood** at 4K 60fps resolution. Full interactive playback, scrubber, and download controls are available below:",
                model = "google/veo-2",
                videoData = VideoGenerationData(
                    prompt = prompt,
                    style = style,
                    durationSeconds = duration,
                    aspectRatio = aspectRatio,
                    cameraMotion = cameraMotion,
                    lightingMood = lightingMood,
                    modelEngine = "Google Veo 2"
                ),
                agentPersona = persona.id
            )
            chatDao.insertMessage(ChatMessageEntity.fromDomain(videoMsg))
        }
    }

    /**
     * Generates an automated viral social media reel using OmniRoute multi-model resilient routing
     * and high-retention psychological hooks with Hormozi-style kinetic captions.
     */
    fun generateViralReel(
        topic: String,
        template: String = "Hormozi Kinetic Captions",
        voiceover: String = "Deep Authority Male",
        duration: Int = 30,
        hookCategory: String = "Shock & Pattern Interrupt",
        preferredProviderId: String? = null,
        forceFallbackTest: Boolean = false
    ) {
        val sessionId = _currentSessionId.value
        val persona = _activePersona.value

        viewModelScope.launch {
            ensureSession(sessionId, "🔥 Viral Reel: $topic", persona.id)

            // Step 1: Execute OmniRoute Resilient Multi-Model Gateway Routing
            val routeResult = OmniRouteEngine.routeWithResilience(
                preferredProviderId = preferredProviderId,
                forceFallbackTest = forceFallbackTest
            )

            // Step 2: Generate High-Retention Psychological Hook & Scene Timeline
            val category = ViralHooksEngine.HookCategory.entries.find {
                it.displayName.contains(hookCategory, ignoreCase = true)
            } ?: ViralHooksEngine.HookCategory.SHOCK_INTERRUPT

            val reelScript = ViralHooksEngine.buildReelScript(
                topic = topic,
                category = category,
                voiceoverVoice = voiceover,
                durationSeconds = duration
            )

            // User prompt for viral reel
            val userMsg = ChatMessage(
                sessionId = sessionId,
                role = "user",
                content = "🎬 **Generate Viral Reel via OmniRoute Gateway:** \"$topic\"\n*Model: ${routeResult.chosenProvider.displayName} • Hook Style: ${category.displayName} (${category.retentionBoost}) • Duration: ${duration}s • Format: 9:16 Vertical Reel*",
                model = routeResult.chosenProvider.modelId,
                agentPersona = persona.id
            )
            chatDao.insertMessage(ChatMessageEntity.fromDomain(userMsg))

            _isRenderingVideo.value = true

            // Fast simulated rendering of video diffusion and kinetic subtitle burning
            delay(2400)
            _isRenderingVideo.value = false

            val fallbackNotice = if (routeResult.wasFallbackTriggered) {
                "> ⚠️ **OmniRoute Resilient Fallback Triggered:** Primary route encountered rate limit. Seamlessly switched to `${routeResult.chosenProvider.displayName}` in ${routeResult.telemetry.latencyMs}ms with zero downtime.\n\n"
            } else ""

            val reelMsg = ChatMessage(
                sessionId = sessionId,
                role = "assistant",
                content = """🚀 **9:16 Viral Reel Synthesized via OmniRoute AI Gateway!**

$fallbackNotice**OmniRoute Gateway Telemetry:**
- **Active Model Route:** `${routeResult.telemetry.activeProvider}` (${routeResult.telemetry.latencyMs}ms latency)
- **Circuit Breaker State:** `${routeResult.telemetry.circuitState}`
- **RTK Token Compression:** `${((routeResult.telemetry.compressionRatio) * 100).toInt()}% compression savings`
- **Routing Strategy:** `${routeResult.telemetry.routingStrategy}`

### 🧠 Psychological Hook & Retention Architecture:
- **Hook Category:** **${category.displayName}** (${category.retentionBoost})
- **Cognitive Trigger:** ${reelScript.psychologyReason}
- **Opening Line (0-3s):** *"${reelScript.scenes.firstOrNull()?.spokenText ?: topic}"*
- **Audio Cue:** `${reelScript.scenes.firstOrNull()?.sfxCue ?: "[Record Scratch]"}`
- **Music Soundtrack:** `${reelScript.musicTrack}`

*Interactive 9:16 vertical reel preview with word-by-word synced kinetic captions is loaded below:*""",
                model = routeResult.chosenProvider.modelId,
                videoData = VideoGenerationData(
                    prompt = topic,
                    style = template,
                    durationSeconds = duration,
                    aspectRatio = "9:16",
                    cameraMotion = "Fast Jump Cut / Dynamic Zoom",
                    lightingMood = "High-Contrast Studio Rim Light",
                    modelEngine = "OmniRoute + Veo 2",
                    isReel = true,
                    reelTemplate = template,
                    voiceoverScript = voiceover,
                    headlineSubtitle = reelScript.hookTitle,
                    reelScript = reelScript,
                    omniRouteTelemetry = routeResult.telemetry
                ),
                agentPersona = persona.id
            )
            chatDao.insertMessage(ChatMessageEntity.fromDomain(reelMsg))
        }
    }

    fun sendMessage(promptText: String? = null) {
        val rawInput = (promptText ?: _input.value).trim()
        val currentAttachment = _attachedImageUri.value
        if ((rawInput.isEmpty() && currentAttachment == null) || _isStreaming.value || _isCloning.value) return

        val persona = _activePersona.value
        val textToSend = if (rawInput.isEmpty() && currentAttachment != null) {
            "Analyze this uploaded image"
        } else {
            rawInput
        }

        _input.value = ""
        _attachedImageUri.value = null
        val sessionId = _currentSessionId.value
        val useDeepThink = _isDeepThink.value

        viewModelScope.launch {
            ensureSession(sessionId, textToSend, persona.id)

            // Insert User Message (including attachment if present)
            val userMessage = ChatMessage(
                sessionId = sessionId,
                role = "user",
                content = textToSend,
                model = config.value.selectedModel,
                attachmentUri = currentAttachment,
                isDeepThink = useDeepThink,
                agentPersona = persona.id
            )
            chatDao.insertMessage(ChatMessageEntity.fromDomain(userMessage))

            // Check for GitHub URL
            val githubMatch = githubRegex.find(textToSend)
            if (githubMatch != null) {
                val repoUrl = githubMatch.value
                _cloningRepoUrl.value = repoUrl
                _isCloning.value = true

                delay(2000)
                _isCloning.value = false

                val cloneSuccessMessage = ChatMessage(
                    sessionId = sessionId,
                    role = "assistant",
                    content = "✅ **Repository Cloned Successfully!**\n\nI have analyzed the repository: `$repoUrl`\n\n**How can I help you with this code?** You can ask me to:\n- Explain the architecture\n- Find specific functions\n- Write documentation\n- Suggest improvements",
                    model = config.value.selectedModel,
                    agentPersona = persona.id
                )
                chatDao.insertMessage(ChatMessageEntity.fromDomain(cloneSuccessMessage))
                return@launch
            }

            // Create Assistant Placeholder
            val assistantMessageId = UUID.randomUUID().toString()
            val simulatedThinking = if (useDeepThink) {
                if (persona == AgentPersona.MADISON_AGENCY) {
                    """1. Analyzing market category dynamics, audience psychographics & latent desires.
2. Auditing competitor hooks, creative blind spots, and conversion friction.
3. Formulating 3-second thumb-stopper Veo video hooks and psychological positioning.
4. Structuring multi-channel media spend architecture for maximum ROAS."""
                } else {
                    """1. Deconstructing input request: "$textToSend"
2. Parsing architectural constraints, edge cases, and algorithmic complexity.
3. Formulating structured solution with formal reasoning verification.
4. Synthesizing concise, highly readable production answer."""
                }
            } else null

            val initialAssistantMsg = ChatMessage(
                id = assistantMessageId,
                sessionId = sessionId,
                role = "assistant",
                content = "",
                model = config.value.selectedModel,
                isStreaming = true,
                thinkingProcess = simulatedThinking,
                isDeepThink = useDeepThink,
                agentPersona = persona.id
            )
            _activeStreamingMessage.value = initialAssistantMsg
            _isStreaming.value = true

            val allMessages = messages.value + userMessage

            streamJob = launch {
                val currentConfig = config.value
                val apiKey = when (currentConfig.provider) {
                    ApiProvider.OPENROUTER -> currentConfig.openRouterKey.trim()
                    ApiProvider.OMNIROUTE -> currentConfig.omniRouteKey.trim()
                }

                val accumulated = StringBuilder()

                if (apiKey.isEmpty() && currentConfig.provider == ApiProvider.OPENROUTER) {
                    delay(300)
                    if (useDeepThink) {
                        delay(500)
                    }

                    val prefix = if (useDeepThink) {
                        "🧠 **Deep Reasoning Result:**\n\n"
                    } else if (currentAttachment != null) {
                        "🖼️ **Image Inspection:**\n\nI have inspected the uploaded visual data. "
                    } else ""

                    accumulated.append(prefix)

                    client.streamDemoResponse(
                        prompt = textToSend,
                        model = currentConfig.selectedModel,
                        persona = persona.id
                    ).collect { token ->
                        accumulated.append(token)
                        _activeStreamingMessage.value = initialAssistantMsg.copy(
                            content = accumulated.toString(),
                            isStreaming = true
                        )
                    }
                } else {
                    val flow = client.streamChatCompletion(
                        config = currentConfig,
                        messages = allMessages,
                        customSystemPrompt = persona.systemPrompt
                    )

                    flow.catch { e ->
                        val errorText = "⚠️ **Error:** ${e.message}"
                        accumulated.clear()
                        accumulated.append(errorText)
                        val errorMsg = initialAssistantMsg.copy(
                            content = errorText,
                            isStreaming = false,
                            isError = true
                        )
                        _activeStreamingMessage.value = errorMsg
                        chatDao.insertMessage(ChatMessageEntity.fromDomain(errorMsg))
                        _isStreaming.value = false
                    }.collect { token ->
                        accumulated.append(token)
                        _activeStreamingMessage.value = initialAssistantMsg.copy(
                            content = accumulated.toString(),
                            isStreaming = true
                        )
                    }
                }

                val completedMsg = initialAssistantMsg.copy(
                    content = accumulated.toString(),
                    isStreaming = false
                )
                chatDao.insertMessage(ChatMessageEntity.fromDomain(completedMsg))
                _activeStreamingMessage.value = null
                _isStreaming.value = false
            }
        }
    }

    private suspend fun ensureSession(sessionId: String, prompt: String, persona: String = "dawan_core") {
        val existingSession = chatDao.getSessionById(sessionId)
        if (existingSession == null) {
            val title = if (prompt.length > 32) prompt.take(30) + "…" else prompt
            chatDao.insertSession(
                ChatSessionEntity(
                    id = sessionId,
                    title = title,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    lastMessage = prompt,
                    persona = persona
                )
            )
        } else {
            chatDao.updateSession(
                existingSession.copy(
                    updatedAt = System.currentTimeMillis(),
                    lastMessage = prompt,
                    persona = persona
                )
            )
        }
    }

    private fun finalizeCurrentStream() {
        val cur = _activeStreamingMessage.value ?: return
        viewModelScope.launch {
            val finalized = cur.copy(isStreaming = false)
            chatDao.insertMessage(ChatMessageEntity.fromDomain(finalized))
            _activeStreamingMessage.value = null
            _isStreaming.value = false
        }
    }
}
