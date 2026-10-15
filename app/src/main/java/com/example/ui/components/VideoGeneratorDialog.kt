package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.CrimsonRose
import com.example.ui.theme.DreamyPink
import com.example.ui.theme.GlowOrange
import com.example.ui.theme.GlowPink
import com.example.ui.theme.OceanicGreen
import com.example.ui.theme.OceanicLight
import com.example.ui.theme.SendButtonGradient
import com.example.ui.theme.SunsetOrange
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun VideoGeneratorDialog(
    initialPrompt: String = "",
    onGenerate: (prompt: String, style: String, duration: Int, aspectRatio: String, cameraMotion: String, lightingMood: String) -> Unit,
    onDismiss: () -> Unit
) {
    var prompt by remember {
        mutableStateOf(
            initialPrompt.ifBlank { "Cinematic drone hyperlapse flying through a neon Tokyo skyline in rain, volumetric reflections, photorealistic 8K" }
        )
    }

    val cameraMotions = listOf(
        "Dynamic Tracking Shot",
        "Drone Flyover",
        "360 Orbit",
        "Slow Push-In",
        "Crane Pan Up"
    )
    var selectedCameraMotion by remember { mutableStateOf(cameraMotions[0]) }

    val lightingMoods = listOf(
        "Moody Cinematic Glow",
        "Golden Hour Sun",
        "Cyberpunk Neon Fog",
        "Studio Glamour",
        "Natural Overcast"
    )
    var selectedLightingMood by remember { mutableStateOf(lightingMoods[0]) }

    val styles = listOf("Veo Photorealistic", "Cinematic Film (Veo)", "Commercial Ad", "Anime Hyper-Real", "3D Pixar Ultra")
    var selectedStyle by remember { mutableStateOf(styles[0]) }

    val durations = listOf(4, 6, 8, 12)
    var selectedDuration by remember { mutableIntStateOf(6) }

    val aspectRatios = listOf("16:9", "9:16", "1:1")
    var selectedAspectRatio by remember { mutableStateOf("16:9") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, SunsetOrange.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .testTag("video_generator_dialog"),
            color = Color(0xFF181818)
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                // Header with Google Veo 2 Branding
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = SunsetOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Google Veo Video Studio",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    brush = Brush.horizontalGradient(listOf(GlowOrange, CrimsonRose))
                                )
                            )
                        }
                        Text(
                            text = "Next-gen generative video model by Google",
                            fontSize = 11.sp,
                            color = OceanicLight
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                // Veo Engine Badge Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF222222))
                        .border(1.dp, Color(0xFF333333), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = GlowPink,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Veo 2 Generative Diffusion",
                                color = TextWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Brush.horizontalGradient(listOf(SunsetOrange, CrimsonRose)))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "VEO ACTIVE",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Prompt Input
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "VEO VIDEO PROMPT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = TextMuted
                    )
                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        placeholder = { Text("Describe the action, scene dynamics, and subject…", color = Color(0xFF555555)) },
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedContainerColor = Color(0xFF111111),
                            unfocusedContainerColor = Color(0xFF111111),
                            focusedBorderColor = SunsetOrange,
                            unfocusedBorderColor = SurfaceBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("video_prompt_input")
                    )
                }

                // Camera Motion Controls (Veo Signature Feature)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "CAMERA MOVEMENT (VEO OPTICAL FLOW)",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = TextMuted
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(cameraMotions) { motion ->
                            val isSelected = motion == selectedCameraMotion
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) OceanicGreen else Color(0xFF222222))
                                    .border(1.dp, if (isSelected) OceanicLight else SurfaceBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedCameraMotion = motion }
                                    .padding(horizontal = 11.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = motion,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextWhite
                                )
                            }
                        }
                    }
                }

                // Lighting & Mood
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "LIGHTING & ATMOSPHERE",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = TextMuted
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(lightingMoods) { mood ->
                            val isSelected = mood == selectedLightingMood
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) SunsetOrange else Color(0xFF222222))
                                    .border(1.dp, if (isSelected) SunsetOrange else SurfaceBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedLightingMood = mood }
                                    .padding(horizontal = 11.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = mood,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextWhite
                                )
                            }
                        }
                    }
                }

                // Duration & Aspect Ratio Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Duration
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "DURATION",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = TextMuted
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            durations.forEach { d ->
                                val isSelected = d == selectedDuration
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) CrimsonRose else Color(0xFF222222))
                                        .border(1.dp, if (isSelected) CrimsonRose else SurfaceBorder, RoundedCornerShape(6.dp))
                                    .clickable { selectedDuration = d }
                                    .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${d}s",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    // Aspect Ratio
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "RATIO",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = TextMuted
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            aspectRatios.forEach { ar ->
                                val isSelected = ar == selectedAspectRatio
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) OceanicGreen else Color(0xFF222222))
                                        .border(1.dp, if (isSelected) OceanicGreen else SurfaceBorder, RoundedCornerShape(6.dp))
                                    .clickable { selectedAspectRatio = ar }
                                    .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ar,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Veo Generate Action Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SendButtonGradient)
                        .clickable(enabled = prompt.isNotBlank()) {
                            onGenerate(
                                prompt.trim(),
                                selectedStyle,
                                selectedDuration,
                                selectedAspectRatio,
                                selectedCameraMotion,
                                selectedLightingMood
                            )
                            onDismiss()
                        }
                        .testTag("submit_generate_video_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Generate with Google Veo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
