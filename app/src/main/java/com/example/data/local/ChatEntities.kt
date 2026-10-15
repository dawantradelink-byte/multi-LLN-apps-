package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.ChatMessage
import com.example.data.model.ChatSession
import com.example.data.model.VideoGenerationData

@Entity(tableName = "chat_sessions")
data class ChatSessionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long,
    val lastMessage: String,
    val persona: String = "dawan_core"
) {
    fun toDomain() = ChatSession(
        id = id,
        title = title,
        createdAt = createdAt,
        updatedAt = updatedAt,
        lastMessage = lastMessage,
        persona = persona
    )

    companion object {
        fun fromDomain(session: ChatSession) = ChatSessionEntity(
            id = session.id,
            title = session.title,
            createdAt = session.createdAt,
            updatedAt = session.updatedAt,
            lastMessage = session.lastMessage,
            persona = session.persona
        )
    }
}

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val role: String,
    val content: String,
    val timestamp: Long,
    val model: String,
    val isError: Boolean,
    val attachmentUri: String? = null,
    val thinkingProcess: String? = null,
    val isDeepThink: Boolean = false,
    val videoPrompt: String? = null,
    val videoStyle: String? = null,
    val videoDuration: Int? = null,
    val videoAspectRatio: String? = null,
    val videoCameraMotion: String? = null,
    val videoLightingMood: String? = null,
    val videoModelEngine: String? = null,
    val videoIsReel: Boolean = false,
    val videoReelTemplate: String? = null,
    val videoVoiceoverScript: String? = null,
    val videoHeadlineSubtitle: String? = null,
    val agentPersona: String = "dawan_core"
) {
    fun toDomain(isStreaming: Boolean = false) = ChatMessage(
        id = id,
        sessionId = sessionId,
        role = role,
        content = content,
        timestamp = timestamp,
        model = model,
        isStreaming = isStreaming,
        isError = isError,
        attachmentUri = attachmentUri,
        thinkingProcess = thinkingProcess,
        isDeepThink = isDeepThink,
        videoData = if (videoPrompt != null) {
            VideoGenerationData(
                prompt = videoPrompt,
                style = videoStyle ?: "Cinematic (Veo)",
                durationSeconds = videoDuration ?: 6,
                aspectRatio = videoAspectRatio ?: "16:9",
                cameraMotion = videoCameraMotion ?: "Dynamic Tracking Shot",
                lightingMood = videoLightingMood ?: "Moody Cinematic Glow",
                modelEngine = videoModelEngine ?: "Google Veo 2",
                isReel = videoIsReel,
                reelTemplate = videoReelTemplate,
                voiceoverScript = videoVoiceoverScript,
                headlineSubtitle = videoHeadlineSubtitle
            )
        } else null,
        agentPersona = agentPersona
    )

    companion object {
        fun fromDomain(message: ChatMessage) = ChatMessageEntity(
            id = message.id,
            sessionId = message.sessionId,
            role = message.role,
            content = message.content,
            timestamp = message.timestamp,
            model = message.model,
            isError = message.isError,
            attachmentUri = message.attachmentUri,
            thinkingProcess = message.thinkingProcess,
            isDeepThink = message.isDeepThink,
            videoPrompt = message.videoData?.prompt,
            videoStyle = message.videoData?.style,
            videoDuration = message.videoData?.durationSeconds,
            videoAspectRatio = message.videoData?.aspectRatio,
            videoCameraMotion = message.videoData?.cameraMotion,
            videoLightingMood = message.videoData?.lightingMood,
            videoModelEngine = message.videoData?.modelEngine,
            videoIsReel = message.videoData?.isReel ?: false,
            videoReelTemplate = message.videoData?.reelTemplate,
            videoVoiceoverScript = message.videoData?.voiceoverScript,
            videoHeadlineSubtitle = message.videoData?.headlineSubtitle,
            agentPersona = message.agentPersona
        )
    }
}
