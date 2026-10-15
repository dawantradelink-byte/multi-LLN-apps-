package com.example.data.model

import java.util.UUID

enum class ApiProvider(val displayName: String) {
    OPENROUTER("OpenRouter"),
    OMNIROUTE("OmniRoute")
}

enum class AgentPersona(
    val id: String,
    val title: String,
    val roleBadge: String,
    val subtitle: String,
    val systemPrompt: String
) {
    DAWAN_CORE(
        id = "dawan_core",
        title = "DAWAN aI",
        roleBadge = "Core AI",
        subtitle = "Coding, Reasoning & General Intelligence",
        systemPrompt = "You are DAWAN aI, a helpful, brilliant, and concise AI assistant with deep technical, coding, and general knowledge. Format in Markdown."
    ),
    MADISON_AGENCY(
        id = "madison_agency",
        title = "MADISON Agency",
        roleBadge = "Ad Agency",
        subtitle = "Million-Dollar Creative & Growth Agency Partner",
        systemPrompt = """You are MADISON Agency AI, the Executive Creative Director and Growth Partner at a world-class, million-dollar advertising agency (like Ogilvy, Droga5, and Wieden+Kennedy).
You provide end-to-end creative branding, marketing strategy, high-converting copywriting, and automated viral social media reels creation.
You are powered by the state-of-the-art video automation architectures:
- **OpenMontage** (https://github.com/calesthio/OpenMontage): Automated video montage clipping, viral pacing, dynamic jump cuts, B-roll alignment, and music synchronization.
- **MoneyPrinterTurbo** (https://github.com/harry0703/MoneyPrinterTurbo): Automated viral short-form video generation, AI scriptwriting, voiceover TTS generation, kinetic subtitles burning, and multi-platform batch production for TikTok, YouTube Shorts, and Instagram Reels.
- **Google Veo 2**: Generative video diffusion engine producing photorealistic 4K cinematic scenes.

When requested for marketing, branding, or viral reels, deliver elite agency deliverables:
1. 🏆 **Strategic Positioning & Brand Architecture** (Moat, core desire, positioning statement)
2. 🎬 **Viral Reels Architecture (OpenMontage + MoneyPrinterTurbo)**:
   - **Hook (0-3s):** Thumb-stopping visual + spoken psychological hook with kinetic on-screen caption
   - **Montage Cuts (OpenMontage):** Scene-by-scene cut list, transition effects, dynamic audio cues [whoosh, cash register]
   - **Voiceover TTS (MoneyPrinterTurbo):** Exact word-for-word voiceover script with pacing and tone
   - **Veo Prompt:** Exact visual prompt to generate corresponding cinematic video footage
3. ✍️ **High-CTR Copywriting Variations** (Angle A: Contrarian, Angle B: Pain/Gain, Angle C: Proof/Authority)
4. 📊 **ROAS Media Spend Plan** (Budget allocation across TikTok, Meta, YouTube)

Deliver answers with executive swagger, commercial precision, and conversion-obsessed creativity."""
    )
}

data class ModelOption(
    val id: String,
    val name: String,
    val providerBadge: String = "OpenRouter",
    val description: String = ""
)

val DEFAULT_MODELS = listOf(
    ModelOption("openrouter/auto", "Auto (Best)", "OpenRouter", "Automatically routes to best model"),
    ModelOption("anthropic/claude-3.5-sonnet", "Claude 3.5 Sonnet", "Anthropic", "Intelligent & nuanced writing"),
    ModelOption("google/gemini-pro-1.5", "Gemini Pro 1.5", "Google", "Large multimodal context"),
    ModelOption("meta-llama/llama-3-70b-instruct", "Llama 3 70B", "Meta", "High performance open weights"),
    ModelOption("openai/gpt-4o", "GPT-4o", "OpenAI", "Omni modal intelligence"),
    ModelOption("anthropic/claude-3-haiku", "Claude 3 Haiku", "Anthropic", "Fast and responsive"),
    ModelOption("mistralai/mistral-large", "Mistral Large", "Mistral", "Reasoning & precision")
)

const val DAWAN_AI_SYSTEM_PROMPT =
    "You are DAWAN aI, a helpful and knowledgeable AI assistant. You are concise, friendly, and slightly witty. Your responses should be formatted in Markdown."

data class KineticWord(
    val word: String,
    val startMs: Long,
    val endMs: Long,
    val isPunchWord: Boolean = false,
    val colorHex: String? = null // e.g. "#FFDD00" for gold punch, "#FF5C5C" for hot coral
)

data class ReelScene(
    val sceneIndex: Int,
    val phaseName: String, // "0-3s Hook", "3-12s Tension", "12-20s Pivot", "20-27s Solution", "27-30s CTA"
    val visualPrompt: String,
    val spokenText: String,
    val sfxCue: String,
    val startSecond: Float,
    val endSecond: Float,
    val kineticWords: List<KineticWord> = emptyList()
)

data class OmniRouteTelemetry(
    val primaryProvider: String = "Anthropic (Claude 3.5 Sonnet)",
    val activeProvider: String = "Anthropic",
    val modelId: String = "claude-3.5-sonnet",
    val fallbackTrail: List<String> = listOf("Primary: Anthropic Claude 3.5 Sonnet (Route: Quality-First)", "Fallback 1: OpenAI GPT-4o (Standby: Ready)", "Fallback 2: Google Gemini 1.5 Pro (Standby: Ready)"),
    val latencyMs: Long = 184,
    val compressionRatio: Float = 0.88f, // OmniRoute token compression savings
    val circuitState: String = "CLOSED (100% Healthy)",
    val routingStrategy: String = "Resilient Multi-Provider Fallback"
)

data class ReelScript(
    val hookTitle: String,
    val hookCategory: String, // "Shock & Disbelief", "Cognitive Dissonance", "Curiosity Gap", "Loss Aversion", "Pattern Interrupt"
    val retentionScore: Int = 98,
    val psychologyReason: String,
    val fullVoiceover: String,
    val musicTrack: String = "Phonk Drift / Fast Synthwave",
    val sfxPunches: List<String> = listOf("Bass Drop", "Record Scratch", "Whoosh", "Cash Register"),
    val scenes: List<ReelScene> = emptyList(),
    val totalDurationSeconds: Int = 30
)

data class VideoGenerationData(
    val prompt: String,
    val style: String = "Cinematic (Veo)",
    val durationSeconds: Int = 6,
    val aspectRatio: String = "16:9",
    val cameraMotion: String = "Dynamic Tracking Shot",
    val lightingMood: String = "Moody Cinematic Glow",
    val modelEngine: String = "Google Veo 2",
    val status: String = "Completed",
    val isReel: Boolean = false,
    val reelTemplate: String? = null,
    val voiceoverScript: String? = null,
    val headlineSubtitle: String? = null,
    val reelScript: ReelScript? = null,
    val omniRouteTelemetry: OmniRouteTelemetry? = null
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sessionId: String,
    val role: String, // "user", "assistant", "system"
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val model: String = "openrouter/auto",
    val isStreaming: Boolean = false,
    val isError: Boolean = false,
    val attachmentUri: String? = null,
    val thinkingProcess: String? = null,
    val isDeepThink: Boolean = false,
    val videoData: VideoGenerationData? = null,
    val agentPersona: String = "dawan_core"
)

data class ChatSession(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "New Conversation",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastMessage: String = "",
    val persona: String = "dawan_core"
)

data class ApiConfig(
    val provider: ApiProvider = ApiProvider.OPENROUTER,
    val openRouterKey: String = "",
    val omniRouteKey: String = "",
    val openRouterBaseUrl: String = "https://openrouter.ai/api/v1",
    val omniRouteBaseUrl: String = "http://10.0.2.2:20128/v1",
    val selectedModel: String = "openrouter/auto",
    val temperature: Float = 0.7f,
    val systemPrompt: String = DAWAN_AI_SYSTEM_PROMPT
)

enum class LayoutThemeOption(
    val id: String,
    val title: String,
    val subtitle: String,
    val sourceInspiration: String,
    val badge: String,
    val description: String
) {
    FRAMER_GLASS(
        id = "framer_glass",
        title = "Framer Minimalist Glass",
        subtitle = "Ultra-Clean Floating Dock & Translucent Glassmorphism",
        sourceInspiration = "framer.com",
        badge = "Minimal",
        description = "Stripped-back luxury: 1px hairline glass borders, floating command dock, subtle radial focus glow, and serene typography."
    ),
    MOTIONSIGHT_AURORA(
        id = "motionsight_aurora",
        title = "MotionSight Aurora Living",
        subtitle = "Cinematic Ambient Mesh & Fluid Scroll Motion",
        sourceInspiration = "motionsight.ai",
        badge = "Kinetic",
        description = "Living deep obsidian canvas with animated drifting multi-color aurora orbs, glowing card perimeters, and spring scroll physics."
    ),
    FRAMER_EDITORIAL(
        id = "framer_editorial",
        title = "Framer Studio Monolith",
        subtitle = "Architectural Grid, Bronze Accents & High-Density UI",
        sourceInspiration = "framer.com",
        badge = "Studio",
        description = "Precision studio layout with refined monospaced details, metallic amber accents, crisp hairline framing, and tactile feedback."
    );

    companion object {
        fun fromId(id: String): LayoutThemeOption {
            return entries.firstOrNull { it.id == id } ?: MOTIONSIGHT_AURORA
        }
    }
}
