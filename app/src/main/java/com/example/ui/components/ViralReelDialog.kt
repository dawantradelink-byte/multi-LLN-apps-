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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.data.omniroute.OmniRouteEngine
import com.example.data.omniroute.ViralHooksEngine
import com.example.ui.theme.CrimsonRose
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
fun ViralReelDialog(
    initialTopic: String = "",
    onGenerate: (
        topic: String,
        template: String,
        voiceover: String,
        duration: Int,
        hookCategory: String,
        preferredProviderId: String,
        simulateFallback: Boolean
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var topic by remember {
        mutableStateOf(
            initialTopic.ifBlank { "5 Psychological Hooks That Instantly Double Social Media Video Views" }
        )
    }

    val providers = remember { OmniRouteEngine.defaultProviders }
    var selectedProvider by remember { mutableStateOf(providers.first()) }
    var simulateFallback by remember { mutableStateOf(false) }

    val hookCategories = remember { ViralHooksEngine.HookCategory.entries }
    var selectedHookCategory by remember { mutableStateOf(hookCategories.first()) }

    val templates = listOf(
        "Hormozi Kinetic Captions",
        "OpenMontage Rapid Cuts",
        "Split-Screen B-Roll",
        "MoneyPrinterTurbo Auto-TTS",
        "Cinematic Veo Reel"
    )
    var selectedTemplate by remember { mutableStateOf(templates[0]) }

    val voices = listOf(
        "Deep Authority Male",
        "Energetic Creator Female",
        "Storyteller Podcast",
        "Documentary Deep Voice"
    )
    var selectedVoice by remember { mutableStateOf(voices[0]) }

    val durations = listOf(15, 30, 45, 60)
    var selectedDuration by remember { mutableIntStateOf(30) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, GlowOrange.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .testTag("viral_reel_dialog"),
            color = Color(0xFF141417)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = GlowOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Viral Reels Studio",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    brush = Brush.horizontalGradient(listOf(GlowOrange, CrimsonRose))
                                )
                            )
                        }
                        Text(
                            text = "OmniRoute Multi-Model Gateway & Psychological Hooks",
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

                // OmniRoute Resilient Gateway Status Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1C1C22))
                        .border(1.dp, OceanicGreen.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Route,
                                contentDescription = null,
                                tint = OceanicGreen,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "OMNIROUTE AI GATEWAY (358+ PROVIDERS)",
                                color = OceanicGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = "Adaptive multi-provider routing with automated circuit-breaker fallback, RTK compression (~88% tokens saved), and zero downtime.",
                            color = TextWhite.copy(alpha = 0.8f),
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }

                // 1. OmniRoute Model Router Selection
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "OMNIROUTE PRIMARY MODEL ROUTE",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = TextMuted
                        )
                        Text(
                            text = "${selectedProvider.averageLatencyMs}ms avg",
                            fontSize = 10.sp,
                            color = OceanicLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(providers) { p ->
                            val isSelected = p.providerId == selectedProvider.providerId
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) OceanicGreen.copy(alpha = 0.2f) else Color(0xFF222228)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) OceanicGreen else SurfaceBorder,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedProvider = p }
                                    .padding(horizontal = 10.dp, vertical = 7.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = p.displayName,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) OceanicLight else TextWhite
                                    )
                                    Text(
                                        text = p.freeTokensMonthlyPool,
                                        fontSize = 9.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                }

                // Fallback Simulation Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1E1E24))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Test Resilient Cascade Fallback",
                            color = TextWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Simulates 429 quota exhaustion & verifies auto-switch to fallback",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                    Switch(
                        checked = simulateFallback,
                        onCheckedChange = { simulateFallback = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = GlowOrange,
                            checkedTrackColor = GlowOrange.copy(alpha = 0.4f)
                        )
                    )
                }

                // 2. High-Retention Psychological Hook Category
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "VIRAL HOOK PSYCHOLOGY",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = GlowOrange
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        hookCategories.forEach { cat ->
                            val isSelected = cat == selectedHookCategory
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) GlowOrange.copy(alpha = 0.15f) else Color(0xFF202026)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) GlowOrange else SurfaceBorder,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedHookCategory = cat }
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(cat.icon, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = cat.displayName,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) GlowOrange else TextWhite
                                        )
                                        Text(
                                            text = cat.subtitle,
                                            fontSize = 10.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(GlowOrange.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = cat.retentionBoost,
                                        color = GlowOrange,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Reel Topic
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "REEL CORE TOPIC / PRODUCT",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = TextMuted
                    )
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reel_topic_input"),
                        placeholder = {
                            Text(
                                "e.g., Why 99% of creators fail at viral retention...",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GlowOrange,
                            unfocusedBorderColor = SurfaceBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedContainerColor = Color(0xFF1E1E24),
                            unfocusedContainerColor = Color(0xFF1E1E24)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        textStyle = TextStyle(fontSize = 13.sp)
                    )
                }

                // 4. Voiceover & Duration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1.3f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "TTS VOICEOVER",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = TextMuted
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(voices) { v ->
                                val isSelected = v == selectedVoice
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CrimsonRose else Color(0xFF222228))
                                        .border(1.dp, if (isSelected) CrimsonRose else SurfaceBorder, RoundedCornerShape(8.dp))
                                        .clickable { selectedVoice = v }
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = v.substringBefore(" "),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "DURATION",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = TextMuted
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            durations.forEach { d ->
                                val isSelected = d == selectedDuration
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) GlowOrange else Color(0xFF222228))
                                        .border(1.dp, if (isSelected) GlowOrange else SurfaceBorder, RoundedCornerShape(8.dp))
                                        .clickable { selectedDuration = d }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${d}s",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else TextWhite
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Render Action Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SendButtonGradient)
                        .clickable(enabled = topic.isNotBlank()) {
                            onGenerate(
                                topic.trim(),
                                selectedTemplate,
                                selectedVoice,
                                selectedDuration,
                                selectedHookCategory.displayName,
                                selectedProvider.providerId,
                                simulateFallback
                            )
                            onDismiss()
                        }
                        .testTag("submit_generate_reel_button"),
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
                            text = "Synthesize Viral Reel (9:16)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
