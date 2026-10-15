package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DawanDarkColorScheme = darkColorScheme(
    primary = CrimsonRose,
    onPrimary = TextWhite,
    primaryContainer = SurfaceDarkElevated,
    onPrimaryContainer = TextWhite,
    secondary = SunsetOrange,
    onSecondary = TextWhite,
    tertiary = DreamyPink,
    background = BackgroundDark,
    onBackground = TextWhite,
    surface = SurfaceDark,
    onSurface = TextWhite,
    surfaceVariant = SurfaceDarkElevated,
    onSurfaceVariant = TextMuted,
    outline = SurfaceBorder,
    outlineVariant = DividerDark
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DawanDarkColorScheme,
        typography = DawanAiTypography,
        content = content
    )
}
