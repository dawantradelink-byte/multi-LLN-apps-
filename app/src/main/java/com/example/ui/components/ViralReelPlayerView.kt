package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.KineticWord
import com.example.data.model.OmniRouteTelemetry
import com.example.data.model.ReelScene
import com.example.data.model.ReelScript
import com.example.ui.theme.CrimsonRose
import com.example.ui.theme.GlowOrange
import com.example.ui.theme.GlowPink
import com.example.ui.theme.OceanicGreen
import com.example.ui.theme.OceanicLight
import com.example.ui.theme.SunsetOrange
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * 9:16 Interactive Viral Reel Player View
 *
 * Provides:
 * - Real-time word-by-word Hormozi-style glowing kinetic captions
 * - Audio visualizer waveform bars reacting to voiceover tempo
 * - Interactive scene timeline scrubber (Hook -> Tension -> Pivot -> Solution -> CTA)
 * - Reels Engagement HUD (Likes, Shares, Audio Disc, SFX Triggers)
 * - OmniRoute Multi-Model Routing Telemetry Inspector
 * - Script, Prompts & Subtitles Export Dialog
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ViralReelPlayerView(
    script: ReelScript,
    telemetry: OmniRouteTelemetry?,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var currentPositionMs by remember { mutableLongStateOf(0L) }
    val totalDurationMs = (script.totalDurationSeconds * 1000L).coerceAtLeast(30000L)

    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(142800) }
    var isBookmarked by remember { mutableStateOf(false) }
    var showTelemetryModal by remember { mutableStateOf(false) }
    var showExportModal by remember { mutableStateOf(false) }

    // Playback ticker loop
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(50)
            currentPositionMs = (currentPositionMs + 50)
            if (currentPositionMs >= totalDurationMs) {
                currentPositionMs = 0L // Loop playback
            }
        }
    }

    // Active Scene calculation
    val currentSecond = currentPositionMs / 1000f
    val activeScene = remember(currentSecond, script) {
        script.scenes.find { currentSecond >= it.startSecond && currentSecond < it.endSecond }
            ?: script.scenes.lastOrNull()
            ?: ReelScene(1, "Hook", "", "", "", 0f, 3f)
    }

    // Rotating audio disc animation
    val infiniteTransition = rememberInfiniteTransition(label = "reel_disc")
    val discRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disc_spin"
    )

    // Pulsing gradient mesh background animation
    val bgShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_shift"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0C0C0F))
            .border(1.5.dp, GlowOrange.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .testTag("viral_reel_player_view")
    ) {
        // 9:16 Aspect Ratio Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f)
                .clickable { isPlaying = !isPlaying }
        ) {
            // Scene Dynamic Gradient Background (simulating generative video visuals)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Dynamic background shift based on active scene index
                val sceneColors = when (activeScene.sceneIndex) {
                    1 -> listOf(Color(0xFF2E0818), Color(0xFF5A1A0F), Color(0xFF0B0914))
                    2 -> listOf(Color(0xFF1E0E3E), Color(0xFF38122D), Color(0xFF080B17))
                    3 -> listOf(Color(0xFF0B2E24), Color(0xFF173819), Color(0xFF07111A))
                    4 -> listOf(Color(0xFF33200B), Color(0xFF4A180E), Color(0xFF0E0B1A))
                    else -> listOf(Color(0xFF260D38), Color(0xFF4C122D), Color(0xFF0F0B18))
                }

                drawRect(
                    brush = Brush.radialGradient(
                        colors = sceneColors,
                        center = Offset(w * (0.3f + bgShift * 0.4f), h * (0.4f + bgShift * 0.2f)),
                        radius = h * 0.75f
                    )
                )

                // Ambient light particle accents
                drawCircle(
                    color = GlowOrange.copy(alpha = 0.18f),
                    radius = 90.dp.toPx(),
                    center = Offset(w * 0.8f, h * 0.25f)
                )
                drawCircle(
                    color = OceanicGreen.copy(alpha = 0.14f),
                    radius = 120.dp.toPx(),
                    center = Offset(w * 0.2f, h * 0.75f)
                )
            }

            // Top Bar: OmniRoute Model Pill + Retention Badge + Close
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // OmniRoute Model Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .border(1.dp, OceanicGreen.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                        .clickable { showTelemetryModal = true }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(OceanicGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = telemetry?.modelId ?: "OmniRoute: Claude 3.5 Sonnet",
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${telemetry?.latencyMs ?: 180}ms",
                        color = OceanicLight,
                        fontSize = 10.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Retention Score Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(GlowOrange.copy(alpha = 0.25f))
                            .border(1.dp, GlowOrange, RoundedCornerShape(14.dp))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "🔥 ${script.retentionScore}% Hook",
                            color = GlowOrange,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (onDismiss != null) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // SFX & Scene Trigger Banner (flashes when approaching scene transition)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 54.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black.copy(alpha = 0.65f))
                        .border(1.dp, SunsetOrange.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = GlowPink,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${activeScene.phaseName} • ${activeScene.sfxCue}",
                        color = GlowPink,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Center Stage: Hormozi-Style Synced Kinetic Captions
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.Center)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Hook Title Header
                    Text(
                        text = script.hookCategory.uppercase(),
                        color = SunsetOrange,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )

                    // Word-by-word Kinetic Captions
                    FlowRow(
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        activeScene.kineticWords.forEach { word ->
                            val isActive = currentPositionMs in word.startMs..word.endMs
                            val isPast = currentPositionMs > word.endMs

                            val wordColor = when {
                                isActive && word.colorHex != null -> Color(android.graphics.Color.parseColor(word.colorHex))
                                isActive -> GlowOrange
                                isPast -> Color.White
                                else -> Color.White.copy(alpha = 0.45f)
                            }

                            val scale = if (isActive && word.isPunchWord) 1.22f else if (isActive) 1.08f else 1.0f

                            Text(
                                text = "${word.word} ",
                                color = wordColor,
                                fontSize = if (word.isPunchWord) 24.sp else 19.sp,
                                fontWeight = if (word.isPunchWord || isActive) FontWeight.Black else FontWeight.Bold,
                                modifier = Modifier
                                    .scale(scale)
                                    .padding(horizontal = 2.dp, vertical = 2.dp)
                                    .then(
                                        if (isActive) {
                                            Modifier.shadow(
                                                elevation = 8.dp,
                                                shape = RoundedCornerShape(4.dp),
                                                ambientColor = wordColor,
                                                spotColor = wordColor
                                            )
                                        } else Modifier
                                    )
                            )
                        }
                    }

                    // Visual scene cue prompt summary
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "🎬 ${activeScene.visualPrompt.take(85)}...",
                        color = TextWhite.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 13.sp,
                        maxLines = 2
                    )
                }
            }

            // Right-Side Social Engagement Bar (TikTok / Reels Vibe)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 85.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Creator Avatar with follow indicator
                Box(contentAlignment = Alignment.BottomCenter) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(GlowOrange, CrimsonRose))
                            )
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("AI", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(GlowPink)
                            .border(1.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Like Button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = {
                            isLiked = !isLiked
                            likeCount += if (isLiked) 1 else -1
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) CrimsonRose else Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Text(
                        text = if (isLiked) "142.9K" else "142.8K",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Bookmark / Save
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { isBookmarked = !isBookmarked },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (isBookmarked) GlowOrange else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "18.2K",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Export / Share Button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { showExportModal = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Export & Share",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Share",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Spinning Vinyl Music Disc
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .rotate(discRotation)
                        .clip(CircleShape)
                        .background(Color(0xFF1E1E24))
                        .border(1.5.dp, GlowOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Audio track",
                        tint = GlowPink,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Bottom Overlay: Sound Title + Audio Waveform + Scene Scrubber + Playhead
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f), Color.Black)
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Sound & Music Track Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(0.75f)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = GlowOrange,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = script.musicTrack,
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }

                // Audio Waveform Equalizer (14 animated bars)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    val bars = 14
                    for (i in 0 until bars) {
                        val barHeight = remember(currentPositionMs, i) {
                            if (isPlaying) {
                                val seed = ((currentPositionMs / 80 + i * 7) % 100).toFloat()
                                (4 + (seed * 0.12f)).dp
                            } else {
                                3.dp
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(barHeight)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (i % 2 == 0) GlowOrange else OceanicGreen
                                )
                        )
                    }
                }

                // Scene Jump Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(script.scenes) { scene ->
                        val isCurrent = activeScene.sceneIndex == scene.sceneIndex
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isCurrent) GlowOrange.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.1f)
                                )
                                .border(
                                    1.dp,
                                    if (isCurrent) GlowOrange else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    currentPositionMs = (scene.startSecond * 1000).toLong()
                                }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = scene.phaseName,
                                color = if (isCurrent) GlowOrange else TextWhite.copy(alpha = 0.7f),
                                fontSize = 10.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Playback Progress Slider & Time indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = GlowOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Slider(
                        value = currentPositionMs.toFloat(),
                        onValueChange = { currentPositionMs = it.toLong() },
                        valueRange = 0f..totalDurationMs.toFloat(),
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = GlowOrange,
                            activeTrackColor = GlowOrange,
                            inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${currentPositionMs / 1000}s / ${totalDurationMs / 1000}s",
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    // Modal: OmniRoute Multi-Model Resilient Telemetry Inspector
    if (showTelemetryModal) {
        Dialog(
            onDismissRequest = { showTelemetryModal = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, OceanicGreen.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                color = Color(0xFF141416)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Route,
                                contentDescription = null,
                                tint = OceanicGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "OmniRoute Telemetry",
                                color = TextWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(
                            onClick = { showTelemetryModal = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextMuted
                            )
                        }
                    }

                    // Status Cards
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1D1D22))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Active Provider", color = TextMuted, fontSize = 12.sp)
                                Text(
                                    telemetry?.activeProvider ?: "Anthropic Claude 3.5 Sonnet",
                                    color = OceanicGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Latency", color = TextMuted, fontSize = 12.sp)
                                Text("${telemetry?.latencyMs ?: 180} ms", color = TextWhite, fontSize = 12.sp)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("RTK Compression", color = TextMuted, fontSize = 12.sp)
                                Text("${((telemetry?.compressionRatio ?: 0.88f) * 100).toInt()}% saved", color = GlowOrange, fontSize = 12.sp)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Circuit Breaker", color = TextMuted, fontSize = 12.sp)
                                Text(telemetry?.circuitState ?: "CLOSED (Healthy)", color = OceanicLight, fontSize = 12.sp)
                            }
                        }
                    }

                    Text(
                        text = "RESILIENT FALLBACK CASCADE",
                        color = SunsetOrange,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    telemetry?.fallbackTrail?.forEachIndexed { idx, step ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(if (idx == 0) OceanicGreen else SunsetOrange.copy(alpha = 0.4f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = step,
                                color = TextWhite.copy(alpha = 0.9f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }

    // Modal: Export Reel Script, TTS & Prompts
    if (showExportModal) {
        Dialog(
            onDismissRequest = { showExportModal = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, GlowOrange.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                color = Color(0xFF16161A)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = GlowOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Export Viral Reel Assets",
                                color = TextWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(
                            onClick = { showExportModal = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextMuted
                            )
                        }
                    }

                    // Copy Action Buttons
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // 1. Copy Full Voiceover Script
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF222228))
                                .clickable {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Reel Voiceover", script.fullVoiceover))
                                    Toast.makeText(context, "Voiceover script copied!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = GlowOrange, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Copy Full Voiceover Script", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Text("Word-by-word synced narration text", color = TextMuted, fontSize = 11.sp)
                                }
                            }
                        }

                        // 2. Copy Veo 2 / Runway Visual Prompts
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF222228))
                                .clickable {
                                    val prompts = script.scenes.joinToString("\n\n") { "Scene ${it.sceneIndex} (${it.phaseName}):\n${it.visualPrompt}" }
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Veo Prompts", prompts))
                                    Toast.makeText(context, "Veo 2 visual prompts copied!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Movie, contentDescription = null, tint = CrimsonRose, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Copy Veo 2 Camera Prompts", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Text("Scene-by-scene 4K diffusion generation prompts", color = TextMuted, fontSize = 11.sp)
                                }
                            }
                        }

                        // 3. Copy Kinetic Subtitles JSON
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF222228))
                                .clickable {
                                    val srtData = buildString {
                                        script.scenes.forEach { s ->
                                            appendLine("--- ${s.phaseName} ---")
                                            appendLine("Time: ${s.startSecond}s -> ${s.endSecond}s")
                                            appendLine("SFX: ${s.sfxCue}")
                                            appendLine("Text: ${s.spokenText}\n")
                                        }
                                    }
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Kinetic Subtitles", srtData))
                                    Toast.makeText(context, "Kinetic caption data copied!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Subtitles, contentDescription = null, tint = OceanicGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Copy Kinetic Subtitles & SFX Timeline", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Text("Hormozi timestamps + punch-word markers", color = TextMuted, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
