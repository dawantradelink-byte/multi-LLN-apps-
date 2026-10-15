package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ChatMessage
import com.example.data.model.LayoutThemeOption
import com.example.ui.theme.AiBubbleGradient
import com.example.ui.theme.CrimsonRose
import com.example.ui.theme.GlowOrange
import com.example.ui.theme.OceanicGreen
import com.example.ui.theme.SunsetOrange
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.UserBubbleGradient

@Composable
fun ChatBubble(
    message: ChatMessage,
    onRetry: () -> Unit = {},
    layoutTheme: LayoutThemeOption = LayoutThemeOption.MOTIONSIGHT_AURORA,
    modifier: Modifier = Modifier
) {
    val isUser = message.role == "user"
    val context = LocalContext.current
    var isThinkingExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier.widthIn(max = 330.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            // Header tag for AI
            if (!isUser) {
                val isAgency = message.agentPersona == "madison_agency"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                ) {
                    Icon(
                        imageVector = if (message.isDeepThink) {
                            Icons.Default.Psychology
                        } else if (isAgency) {
                            Icons.Default.AutoAwesome
                        } else {
                            Icons.Default.AutoAwesome
                        },
                        contentDescription = "AI Agent",
                        tint = if (isAgency) SunsetOrange else if (message.isDeepThink) Color(0xFFB388FF) else CrimsonRose,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAgency) {
                            "MADISON Agency • Ad Executive"
                        } else if (message.isDeepThink) {
                            "DAWAN aI • Deep Think"
                        } else {
                            "DAWAN aI • ${cleanModelName(message.model)}"
                        },
                        fontSize = 11.sp,
                        fontWeight = if (isAgency) FontWeight.Bold else FontWeight.Medium,
                        color = if (isAgency) GlowOrange else if (message.isDeepThink) Color(0xFFB388FF) else TextMuted
                    )
                }
            }

            // Attached Image Preview if user uploaded an image
            if (!message.attachmentUri.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, Color(0xFF444444), RoundedCornerShape(14.dp))
                        .background(Color(0xFF1E1E1E))
                ) {
                    AsyncImage(
                        model = message.attachmentUri,
                        contentDescription = "Uploaded Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 240.dp, height = 160.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                }
            }

            // Message Bubble Box with Adaptive Theme Styling
            val bubbleShape = if (isUser) {
                RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = 18.dp,
                    bottomEnd = 4.dp
                )
            } else {
                RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 18.dp
                )
            }

            val bubbleModifier = when (layoutTheme) {
                LayoutThemeOption.MOTIONSIGHT_AURORA -> {
                    Modifier
                        .shadow(
                            elevation = if (isUser) 4.dp else 6.dp,
                            shape = bubbleShape,
                            spotColor = if (isUser) CrimsonRose.copy(alpha = 0.35f) else Color.Black.copy(alpha = 0.25f)
                        )
                        .clip(bubbleShape)
                        .background(brush = if (isUser) UserBubbleGradient else AiBubbleGradient)
                }
                LayoutThemeOption.FRAMER_GLASS -> {
                    Modifier
                        .shadow(
                            elevation = 3.dp,
                            shape = bubbleShape,
                            spotColor = Color.Black.copy(alpha = 0.4f)
                        )
                        .clip(bubbleShape)
                        .background(
                            if (isUser) Color(0xFF2B1F2D) else Color(0xFF16161C).copy(alpha = 0.92f)
                        )
                        .border(
                            1.dp,
                            if (isUser) SunsetOrange.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.12f),
                            bubbleShape
                        )
                }
                LayoutThemeOption.FRAMER_EDITORIAL -> {
                    Modifier
                        .clip(bubbleShape)
                        .background(
                            if (isUser) Color(0xFF281E15) else Color(0xFF18181C)
                        )
                        .border(
                            1.dp,
                            if (isUser) Color(0xFFE5A962).copy(alpha = 0.55f) else Color.White.copy(alpha = 0.08f),
                            bubbleShape
                        )
                }
            }

            val contentTextColor = if (isUser) {
                TextWhite
            } else {
                when (layoutTheme) {
                    LayoutThemeOption.MOTIONSIGHT_AURORA -> TextDark
                    LayoutThemeOption.FRAMER_GLASS -> Color(0xFFEEEEF2)
                    LayoutThemeOption.FRAMER_EDITORIAL -> Color(0xFFEEEEF2)
                }
            }

            Box(
                modifier = Modifier
                    .then(bubbleModifier)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .testTag(if (isUser) "user_message_bubble" else "ai_message_bubble")
            ) {
                Column {
                    // Deep Think Collapsible Chain of Thought Section
                    if (!isUser && !message.thinkingProcess.isNullOrBlank()) {
                        val thinkingBg = when (layoutTheme) {
                            LayoutThemeOption.MOTIONSIGHT_AURORA -> Color(0x22000000)
                            else -> Color.White.copy(alpha = 0.06f)
                        }
                        val thinkingTextColor = when (layoutTheme) {
                            LayoutThemeOption.MOTIONSIGHT_AURORA -> Color(0xFF333333)
                            else -> Color(0xFFC0C0D0)
                        }
                        val thinkingHeaderColor = when (layoutTheme) {
                            LayoutThemeOption.MOTIONSIGHT_AURORA -> Color(0xFF26103D)
                            else -> Color(0xFFE0E0EC)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(thinkingBg)
                                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isThinkingExpanded = !isThinkingExpanded },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = "Thinking",
                                        tint = if (layoutTheme == LayoutThemeOption.MOTIONSIGHT_AURORA) Color(0xFF4A148C) else SunsetOrange,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Thinking Process",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = thinkingHeaderColor
                                    )
                                }
                                Icon(
                                    imageVector = if (isThinkingExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Expand thinking",
                                    tint = if (layoutTheme == LayoutThemeOption.MOTIONSIGHT_AURORA) Color(0xFF4A148C) else Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            AnimatedVisibility(
                                visible = isThinkingExpanded,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Text(
                                    text = message.thinkingProcess,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = thinkingTextColor,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                        }
                    }

                    if (message.isStreaming && message.content.isEmpty()) {
                        BouncingTypingIndicator()
                    } else {
                        MarkdownContent(
                            text = message.content,
                            textColor = contentTextColor
                        )
                    }

                    // Render Video Player Card inside AI response if video generation was triggered!
                    if (message.videoData != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        VideoPlayerCard(
                            videoData = message.videoData,
                            onRegenerate = onRetry
                        )
                    }
                }
            }

            // Bottom Actions / Error Bar
            if (message.isError) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp, start = 4.dp)
                        .clickable { onRetry() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Retry",
                        tint = CrimsonRose,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tap to retry",
                        fontSize = 11.sp,
                        color = CrimsonRose,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else if (!isUser && message.content.isNotEmpty() && !message.isStreaming) {
                Row(
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable {
                                val clipboard =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("AI Response", message.content))
                                Toast.makeText(context, "Copied response", Toast.LENGTH_SHORT).show()
                            }
                            .padding(vertical = 2.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Message",
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Copy",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BouncingTypingIndicator(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "typing")
    val dot1Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )
    val dot2Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, delayMillis = 150),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )
    val dot3Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, delayMillis = 300),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Row(
        modifier = modifier.padding(vertical = 4.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .offset(y = dot1Offset.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(CrimsonRose)
        )
        Box(
            modifier = Modifier
                .offset(y = dot2Offset.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(SunsetOrange)
        )
        Box(
            modifier = Modifier
                .offset(y = dot3Offset.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(Color(0xFF215C46))
        )
    }
}

private fun cleanModelName(modelId: String): String {
    return when {
        modelId.contains("claude-3.5-sonnet") -> "Claude 3.5 Sonnet"
        modelId.contains("claude-3-haiku") -> "Claude 3 Haiku"
        modelId.contains("gemini-pro-1.5") -> "Gemini Pro 1.5"
        modelId.contains("llama-3-70b") -> "Llama 3 70B"
        modelId.contains("gpt-4o") -> "GPT-4o"
        modelId == "openrouter/auto" -> "Auto Router"
        else -> modelId.substringAfterLast("/")
    }
}
