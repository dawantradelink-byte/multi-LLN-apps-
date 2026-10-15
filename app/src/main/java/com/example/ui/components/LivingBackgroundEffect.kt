package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.data.model.LayoutThemeOption
import kotlin.math.cos
import kotlin.math.sin

/**
 * High-performance animated ambient background effect inspired by
 * Framer.com and MotionSight.ai.
 *
 * Renders living canvas gradients (aurora mesh, radial spotlights, and subtle grids)
 * with zero CPU overhead using hardware-accelerated Compose Canvas.
 */
@Composable
fun LivingBackgroundEffect(
    layoutOption: LayoutThemeOption,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ambient_aurora")

    // Primary oscillating wave
    val time1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f, // 2 * PI
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time1"
    )

    // Secondary counter-oscillating wave
    val time2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time2"
    )

    // Breathing pulse
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        when (layoutOption) {
            LayoutThemeOption.MOTIONSIGHT_AURORA -> {
                // MotionSight.ai: Living multi-spectral aurora mesh with oceanic emerald, sunset amber, and crimson rose
                // Base obsidian black
                drawRect(color = Color(0xFF0C0C0F))

                // Orb 1: Oceanic Green / Emerald (#215C46) drifting upper-left
                val orb1Center = Offset(
                    x = width * (0.28f + 0.18f * cos(time1)),
                    y = height * (0.22f + 0.14f * sin(time1))
                )
                val orb1Radius = width * 0.72f * pulse
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF215C46).copy(alpha = 0.38f),
                            Color(0xFF163E2F).copy(alpha = 0.18f),
                            Color.Transparent
                        ),
                        center = orb1Center,
                        radius = orb1Radius
                    ),
                    center = orb1Center,
                    radius = orb1Radius
                )

                // Orb 2: Sunset Amber / Orange (#EE9521) drifting right-center
                val orb2Center = Offset(
                    x = width * (0.75f + 0.16f * sin(time2)),
                    y = height * (0.45f + 0.18f * cos(time2))
                )
                val orb2Radius = width * 0.65f * pulse
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFEE9521).copy(alpha = 0.28f),
                            Color(0xFF964B00).copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        center = orb2Center,
                        radius = orb2Radius
                    ),
                    center = orb2Center,
                    radius = orb2Radius
                )

                // Orb 3: Crimson Rose (#EF384A) drifting lower-left
                val orb3Center = Offset(
                    x = width * (0.35f + 0.20f * cos(time2 + 1.5f)),
                    y = height * (0.78f + 0.15f * sin(time1 + 0.8f))
                )
                val orb3Radius = width * 0.68f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFEF384A).copy(alpha = 0.25f),
                            Color(0xFF6B1420).copy(alpha = 0.10f),
                            Color.Transparent
                        ),
                        center = orb3Center,
                        radius = orb3Radius
                    ),
                    center = orb3Center,
                    radius = orb3Radius
                )

                // Orb 4: Dream Lilac (#F4D2FF) drifting subtly top right
                val orb4Center = Offset(
                    x = width * (0.60f + 0.15f * sin(time1)),
                    y = height * (0.12f + 0.10f * cos(time2))
                )
                val orb4Radius = width * 0.45f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFF4D2FF).copy(alpha = 0.14f),
                            Color.Transparent
                        ),
                        center = orb4Center,
                        radius = orb4Radius
                    ),
                    center = orb4Center,
                    radius = orb4Radius
                )
            }

            LayoutThemeOption.FRAMER_GLASS -> {
                // Framer.com: Ultra-clean deep obsidian floor with subtle radial spotlight & delicate grid
                drawRect(color = Color(0xFF0F0F12))

                // Top center ambient spotlight
                val spotCenter = Offset(
                    x = width * (0.50f + 0.10f * cos(time1 * 0.6f)),
                    y = height * (0.18f + 0.08f * sin(time1 * 0.6f))
                )
                val spotRadius = width * 0.85f * pulse
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4A3469).copy(alpha = 0.22f),
                            Color(0xFF263A48).copy(alpha = 0.14f),
                            Color.Transparent
                        ),
                        center = spotCenter,
                        radius = spotRadius
                    ),
                    center = spotCenter,
                    radius = spotRadius
                )

                // Soft bottom glow
                val bottomCenter = Offset(
                    x = width * (0.50f + 0.12f * sin(time2 * 0.5f)),
                    y = height * 0.95f
                )
                val bottomRadius = width * 0.6f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF215C46).copy(alpha = 0.18f),
                            Color(0xFFEE9521).copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        center = bottomCenter,
                        radius = bottomRadius
                    ),
                    center = bottomCenter,
                    radius = bottomRadius
                )

                // Subtle grid dots (Framer signature)
                val step = 38f
                var x = 0f
                while (x < width) {
                    var y = 0f
                    while (y < height) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.025f),
                            radius = 1.2f,
                            center = Offset(x, y)
                        )
                        y += step
                    }
                    x += step
                }
            }

            LayoutThemeOption.FRAMER_EDITORIAL -> {
                // Framer Studio Monolith: Architectural dark studio with warm amber & bronze streaks
                drawRect(color = Color(0xFF131315))

                // Warm bronze ambient glow upper right
                val bronzeCenter = Offset(
                    x = width * (0.80f + 0.12f * sin(time1 * 0.7f)),
                    y = height * (0.25f + 0.10f * cos(time2 * 0.7f))
                )
                val bronzeRadius = width * 0.75f * pulse
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF6B451A).copy(alpha = 0.28f),
                            Color(0xFF38230D).copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        center = bronzeCenter,
                        radius = bronzeRadius
                    ),
                    center = bronzeCenter,
                    radius = bronzeRadius
                )

                // Cyber teal ambient accent lower left
                val tealCenter = Offset(
                    x = width * (0.20f + 0.10f * cos(time2 * 0.6f)),
                    y = height * (0.75f + 0.12f * sin(time1 * 0.6f))
                )
                val tealRadius = width * 0.6f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF1B4E42).copy(alpha = 0.22f),
                            Color(0xFF0F2620).copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        center = tealCenter,
                        radius = tealRadius
                    ),
                    center = tealCenter,
                    radius = tealRadius
                )

                // Architectural faint horizontal guide lines
                val lineStep = 72f
                var lineY = 0f
                while (lineY < height) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.018f),
                        start = Offset(0f, lineY),
                        end = Offset(width, lineY),
                        strokeWidth = 1f
                    )
                    lineY += lineStep
                }
            }
        }
    }
}
