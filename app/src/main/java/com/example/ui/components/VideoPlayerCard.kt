package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoGenerationData
import com.example.ui.theme.CrimsonRose
import com.example.ui.theme.DreamyPink
import com.example.ui.theme.GlowOrange
import com.example.ui.theme.OceanicGreen
import com.example.ui.theme.OceanicLight
import com.example.ui.theme.SunsetOrange
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerCard(
    videoData: VideoGenerationData,
    onRegenerate: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var progress by remember { mutableFloatStateOf(0f) }

    // Video playback timer
    val totalSeconds = videoData.durationSeconds.coerceAtLeast(4)
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(50)
            progress += 0.05f / totalSeconds
            if (progress >= 1f) {
                progress = 0f
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "videoScene")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )

    if (videoData.reelScript != null) {
        ViralReelPlayerView(
            script = videoData.reelScript,
            telemetry = videoData.omniRouteTelemetry,
            modifier = modifier
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF161616))
            .border(1.dp, Color(0xFF2E2E2E), RoundedCornerShape(16.dp))
            .padding(12.dp)
            .testTag("video_player_card")
    ) {
        // Video Header with Veo Badges
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (videoData.isReel) {
                                Brush.horizontalGradient(listOf(SunsetOrange, GlowOrange))
                            } else {
                                Brush.horizontalGradient(listOf(SunsetOrange, CrimsonRose))
                            }
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (videoData.isReel) "VIRAL REEL" else "VEO 2",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (videoData.isReel) "OpenMontage • Turbo" else videoData.cameraMotion.ifBlank { videoData.style },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF262626))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "4K 60fps",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanicLight
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF262626))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${totalSeconds}s",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlowOrange
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Animated Video Screen
        val ratio = if (videoData.aspectRatio == "9:16") 0.75f else 1.77f
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0D0D0D))
                .clickable { isPlaying = !isPlaying }
        ) {
            // Dynamic generative shader canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Background gradient
                val gradientBrush = when (videoData.style.lowercase()) {
                    "cyberpunk" -> Brush.verticalGradient(
                        listOf(Color(0xFF090014), Color(0xFF1D002B), Color(0xFF031628))
                    )
                    "anime" -> Brush.verticalGradient(
                        listOf(Color(0xFF1A1A40), Color(0xFF3B1E54), Color(0xFFD4BEE4))
                    )
                    else -> Brush.verticalGradient(
                        listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF020617))
                    )
                }
                drawRect(brush = gradientBrush)

                // Sun / Light source glow
                val sunX = w * 0.5f + (waveOffset - 0.5f) * (w * 0.2f)
                val sunY = h * 0.38f
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(GlowOrange.copy(alpha = 0.85f), CrimsonRose.copy(alpha = 0.4f), Color.Transparent),
                        center = Offset(sunX, sunY),
                        radius = w * 0.28f
                    ),
                    center = Offset(sunX, sunY),
                    radius = w * 0.28f
                )

                // Animated mountain / horizon layers
                val horizonY = h * 0.65f
                drawLine(
                    color = SunsetOrange.copy(alpha = 0.5f),
                    start = Offset(0f, horizonY),
                    end = Offset(w, horizonY),
                    strokeWidth = 2f
                )

                // Moving grid / perspective motion lines
                val numLines = 9
                for (i in 0..numLines) {
                    val startX = (w / numLines) * i
                    drawLine(
                        color = DreamyPink.copy(alpha = 0.22f),
                        start = Offset(startX, horizonY),
                        end = Offset(
                            startX + (i - numLines / 2f) * (w * 0.18f),
                            h
                        ),
                        strokeWidth = 1.5f
                    )
                }

                // Animated light particles / motion streaks
                val particleOffset = (waveOffset * w) % w
                for (p in 1..8) {
                    val px = (p * (w / 8) + particleOffset) % w
                    val py = (p * 37f + (waveOffset * 90f)) % (h * 0.6f) + (h * 0.1f)
                    drawCircle(
                        color = Color.White.copy(alpha = 0.75f),
                        radius = 2.5f,
                        center = Offset(px, py)
                    )
                }
            }

            // Play / Pause Overlay Icon (semi-transparent when playing, prominent when paused)
            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(CrimsonRose.copy(alpha = 0.9f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Hormozi-Style Kinetic Subtitles for Viral Reels (OpenMontage & MoneyPrinterTurbo)
            if (videoData.isReel && isPlaying) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 14.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val captionWords = listOf("STOP", "BUILDING", "WITHOUT", "VIRAL", "SYSTEMS! 🚀", "OPENMONTAGE", "AUTO-CUTS", "REELS 🔥")
                    val activeIndex = ((progress * captionWords.size * 2).toInt()) % captionWords.size
                    Text(
                        text = captionWords[activeIndex],
                        color = Color(0xFFFFEB3B),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Bottom Timeline Scrubber inside video
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Toggle Play",
                        tint = Color.White,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { isPlaying = !isPlaying }
                    )

                    val currentSec = (progress * totalSeconds).toInt()
                    Text(
                        text = "00:0${currentSec} / 00:0${totalSeconds}",
                        color = Color(0xFFDDDDDD),
                        fontSize = 10.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = SunsetOrange,
                    trackColor = Color(0xFF444444)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Prompt description & Veo specs
        Text(
            text = "\"${videoData.prompt}\"",
            color = Color(0xFFE0E0E0),
            fontSize = 12.5.sp,
            lineHeight = 17.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Engine: ${videoData.modelEngine} • Lighting: ${videoData.lightingMood}",
                fontSize = 10.5.sp,
                color = OceanicLight
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Action Buttons: Download MP4 & Regenerate
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF222222))
                    .clickable {
                        Toast.makeText(context, "Saving video to Gallery… (Simulated)", Toast.LENGTH_SHORT).show()
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download",
                    tint = OceanicLight,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Download MP4",
                    color = TextWhite,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF222222))
                    .clickable { onRegenerate() }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Regenerate",
                    tint = SunsetOrange,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Regenerate",
                    color = TextWhite,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
