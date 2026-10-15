package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Background
val BackgroundDark = Color(0xFF1A1A1A)       // Rich dark slate
val SurfaceDark = Color(0xFF111111)          // Deep inner dark
val SurfaceDarkElevated = Color(0xFF222222)  // Card background
val SurfaceBorder = Color(0xFF333333)        // Border outlines

// Vibrant Colorful Palette (Per User Request)
val OceanicGreen = Color(0xFF215C46)         // Oceanic Green / Blue (#215C46)
val SunsetOrange = Color(0xFFEE9521)         // Sunset Orange (#EE9521)
val CrimsonRose = Color(0xFFEF384A)          // Crimson Rose (#EF384A)
val DreamyPink = Color(0xFFF4D2FF)           // Dreamy Pink (#F4D2FF)
val OceanicLight = Color(0xFFA9BDBC)         // Oceanic Light (#A9BDBC)
val GlowPink = Color(0xFFFDACF7)             // Border Glow Pink (#FDACF7)
val GlowOrange = Color(0xFFF8BD8F)           // Border Glow Orange (#F8BD8F)
val PinkishRose = Color(0xFFFD95B2)          // Accent Pink (#FD95B2)

// Text
val TextWhite = Color(0xFFFFFFFF)
val TextDark = Color(0xFF1A1A1A)
val TextMuted = Color(0xFF9E9E9E)
val TextMutedSoft = Color(0xFF6C6C6C)
val DividerDark = Color(0xFF2C2C2C)

// Gradients
val HeaderGradient = Brush.horizontalGradient(
    listOf(OceanicGreen, SunsetOrange, CrimsonRose)
)

val UserBubbleGradient = Brush.linearGradient(
    listOf(CrimsonRose, SunsetOrange)
)

val AiBubbleGradient = Brush.linearGradient(
    listOf(DreamyPink, OceanicLight)
)

val InputBorderGradient = Brush.horizontalGradient(
    listOf(GlowPink, GlowOrange, OceanicLight)
)

val SendButtonGradient = Brush.horizontalGradient(
    listOf(CrimsonRose, SunsetOrange)
)

val EmptyTextGradient = Brush.horizontalGradient(
    listOf(DreamyPink, GlowOrange)
)

val CloningGradient = Brush.horizontalGradient(
    listOf(OceanicGreen, OceanicLight)
)
