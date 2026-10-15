package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ViewQuilt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.LayoutThemeOption
import com.example.ui.theme.CrimsonRose
import com.example.ui.theme.OceanicGreen
import com.example.ui.theme.SunsetOrange
import com.example.ui.theme.TextWhite

/**
 * Interactive Layout Selector modal providing 3 premium layout components
 * imported from Framer.com and MotionSight.ai design patterns.
 */
@Composable
fun LayoutOptionDialog(
    selectedTheme: LayoutThemeOption,
    onSelectTheme: (LayoutThemeOption) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF141418))
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.18f),
                            Color.White.copy(alpha = 0.04f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(OceanicGreen, SunsetOrange, CrimsonRose)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Layout & UI Studio",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Framer.com & MotionSight.ai Systems",
                                fontSize = 11.5.sp,
                                color = Color(0xFFA0A0A8)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_layout_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select from three distinct, ultra-premium layouts featuring living animated backgrounds, fluid scroll physics, and sleek minimalist chrome:",
                    fontSize = 12.5.sp,
                    color = Color(0xFFC0C0C8),
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // The 3 Options
                LayoutThemeOption.entries.forEach { option ->
                    val isSelected = option == selectedTheme
                    LayoutOptionCard(
                        option = option,
                        isSelected = isSelected,
                        onClick = { onSelectTheme(option) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("apply_layout_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SunsetOrange,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Apply & Continue",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun LayoutOptionCard(
    option: LayoutThemeOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        when (option) {
            LayoutThemeOption.FRAMER_GLASS -> Color(0xFFFDACF7)
            LayoutThemeOption.MOTIONSIGHT_AURORA -> SunsetOrange
            LayoutThemeOption.FRAMER_EDITORIAL -> Color(0xFFE5A962)
        }
    } else {
        Color.White.copy(alpha = 0.08f)
    }

    val cardBg = if (isSelected) {
        Color(0xFF1E1E26)
    } else {
        Color(0xFF16161B)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .testTag("layout_option_${option.id}"),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Icon, Title, and Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val iconVector = when (option) {
                        LayoutThemeOption.FRAMER_GLASS -> Icons.Default.Dashboard
                        LayoutThemeOption.MOTIONSIGHT_AURORA -> Icons.Default.AutoAwesome
                        LayoutThemeOption.FRAMER_EDITORIAL -> Icons.Default.ViewQuilt
                    }

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) {
                                    Brush.linearGradient(
                                        listOf(SunsetOrange, CrimsonRose)
                                    )
                                } else {
                                    Brush.linearGradient(
                                        listOf(Color(0xFF262630), Color(0xFF1F1F26))
                                    )
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = option.title,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Source: ${option.sourceInspiration}",
                            fontSize = 10.5.sp,
                            color = if (isSelected) SunsetOrange else Color(0xFF888894),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Selected Radio Indicator
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) SunsetOrange else Color.Transparent
                        )
                        .border(
                            1.5.dp,
                            if (isSelected) SunsetOrange else Color.White.copy(alpha = 0.25f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = option.subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFDFDFE6)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = option.description,
                fontSize = 11.5.sp,
                color = Color(0xFFA0A0B0),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Palette Swatch & Feature Chips Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Feature Badges
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val feature1 = when (option) {
                        LayoutThemeOption.FRAMER_GLASS -> "1px Hairline Glass"
                        LayoutThemeOption.MOTIONSIGHT_AURORA -> "Living Aurora Mesh"
                        LayoutThemeOption.FRAMER_EDITORIAL -> "Architectural Studio"
                    }
                    val feature2 = when (option) {
                        LayoutThemeOption.FRAMER_GLASS -> "Floating Dock"
                        LayoutThemeOption.MOTIONSIGHT_AURORA -> "Kinetic Scroll"
                        LayoutThemeOption.FRAMER_EDITORIAL -> "Bronze Highlights"
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.06f))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = feature1,
                            fontSize = 9.5.sp,
                            color = Color(0xFFD0D0DC),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.06f))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = feature2,
                            fontSize = 9.5.sp,
                            color = Color(0xFFD0D0DC),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Swatch Dots
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    val colors = when (option) {
                        LayoutThemeOption.FRAMER_GLASS -> listOf(
                            Color(0xFF4A3469), Color(0xFF263A48), Color(0xFFFDACF7)
                        )
                        LayoutThemeOption.MOTIONSIGHT_AURORA -> listOf(
                            OceanicGreen, SunsetOrange, CrimsonRose
                        )
                        LayoutThemeOption.FRAMER_EDITORIAL -> listOf(
                            Color(0xFF6B451A), Color(0xFF1B4E42), Color(0xFFE5A962)
                        )
                    }

                    colors.forEach { col ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(col)
                        )
                    }
                }
            }
        }
    }
}
