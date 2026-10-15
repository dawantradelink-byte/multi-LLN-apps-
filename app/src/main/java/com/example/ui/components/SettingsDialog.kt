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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.icons.filled.Palette
import com.example.data.model.ApiConfig
import com.example.data.model.ApiProvider
import com.example.data.model.LayoutThemeOption
import com.example.ui.theme.CrimsonRose
import com.example.ui.theme.DreamyPink
import com.example.ui.theme.GlowOrange
import com.example.ui.theme.OceanicGreen
import com.example.ui.theme.OceanicLight
import com.example.ui.theme.SendButtonGradient
import com.example.ui.theme.SunsetOrange
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceDarkElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun SettingsDialog(
    initialConfig: ApiConfig,
    onSave: (ApiConfig) -> Unit,
    onDismiss: () -> Unit,
    currentLayoutTheme: LayoutThemeOption = LayoutThemeOption.MOTIONSIGHT_AURORA,
    onOpenLayoutPicker: () -> Unit = {}
) {
    var provider by remember { mutableStateOf(initialConfig.provider) }
    var openRouterKey by remember { mutableStateOf(initialConfig.openRouterKey) }
    var omniRouteKey by remember { mutableStateOf(initialConfig.omniRouteKey) }
    var openRouterBaseUrl by remember { mutableStateOf(initialConfig.openRouterBaseUrl) }
    var omniRouteBaseUrl by remember { mutableStateOf(initialConfig.omniRouteBaseUrl) }
    var showKey by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, OceanicGreen.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .testTag("settings_modal"),
            color = Color(0xFF1A1A1A)
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with Gradient Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            brush = Brush.horizontalGradient(listOf(DreamyPink, GlowOrange))
                        )
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("close_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                // Claude Code GitHub Callout Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(OceanicGreen.copy(alpha = 0.2f))
                        .border(1.dp, OceanicGreen, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "GitHub feature",
                            tint = OceanicLight,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(top = 1.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextWhite)) {
                                    append("Claude Code Feature: ")
                                }
                                withStyle(SpanStyle(color = OceanicLight)) {
                                    append("GitHub repository cloning is handled locally and requires ")
                                }
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextWhite)) {
                                    append("NO API Key")
                                }
                                withStyle(SpanStyle(color = OceanicLight)) {
                                    append(". Just paste any repo URL (e.g., https://github.com/facebook/react) in the chat!")
                                }
                            },
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }

                // LLM Provider Picker
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "LLM PROVIDER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = TextMuted
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (provider == ApiProvider.OPENROUTER) CrimsonRose else Color(0xFF111111)
                                )
                                .border(
                                    1.dp,
                                    if (provider == ApiProvider.OPENROUTER) CrimsonRose else SurfaceBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { provider = ApiProvider.OPENROUTER }
                                .padding(vertical = 10.dp)
                                .testTag("provider_openrouter_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "OpenRouter",
                                color = if (provider == ApiProvider.OPENROUTER) Color.White else TextMuted,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (provider == ApiProvider.OMNIROUTE) OceanicGreen else Color(0xFF111111)
                                )
                                .border(
                                    1.dp,
                                    if (provider == ApiProvider.OMNIROUTE) OceanicGreen else SurfaceBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { provider = ApiProvider.OMNIROUTE }
                                .padding(vertical = 10.dp)
                                .testTag("provider_omniroute_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "OmniRoute",
                                color = if (provider == ApiProvider.OMNIROUTE) Color.White else TextMuted,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // API Key Field
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = if (provider == ApiProvider.OPENROUTER) "OPENROUTER / GEMINI API KEY" else "OMNIROUTE API KEY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = TextMuted
                    )
                    OutlinedTextField(
                        value = if (provider == ApiProvider.OPENROUTER) openRouterKey else omniRouteKey,
                        onValueChange = {
                            if (provider == ApiProvider.OPENROUTER) openRouterKey = it else omniRouteKey = it
                        },
                        placeholder = { Text("sk-or-v1-...", color = Color(0xFF555555)) },
                        visualTransformation = if (showKey) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showKey = !showKey }) {
                                Icon(
                                    imageVector = if (showKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Key",
                                    tint = TextMuted
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedContainerColor = Color(0xFF111111),
                            unfocusedContainerColor = Color(0xFF111111),
                            focusedBorderColor = CrimsonRose,
                            unfocusedBorderColor = SurfaceBorder
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("api_key_input")
                    )
                    Text(
                        text = "Required only for AI chat responses, not for GitHub cloning.",
                        fontSize = 10.5.sp,
                        color = Color(0xFF888888)
                    )
                }

                if (provider == ApiProvider.OMNIROUTE) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "OMNIROUTE BASE URL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = TextMuted
                        )
                        OutlinedTextField(
                            value = omniRouteBaseUrl,
                            onValueChange = { omniRouteBaseUrl = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedContainerColor = Color(0xFF111111),
                                unfocusedContainerColor = Color(0xFF111111),
                                focusedBorderColor = OceanicGreen,
                                unfocusedBorderColor = SurfaceBorder
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("omniroute_base_url_input")
                        )
                    }
                }

                // Layout & UI Component Style Card (Framer & MotionSight)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "LAYOUT & UI STYLE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = TextMuted
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF141418))
                            .border(1.dp, SunsetOrange.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable {
                                onDismiss()
                                onOpenLayoutPicker()
                            }
                            .padding(12.dp)
                            .testTag("settings_change_layout_button")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Palette,
                                    contentDescription = null,
                                    tint = SunsetOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = currentLayoutTheme.title,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = "Inspired by ${currentLayoutTheme.sourceInspiration}",
                                        fontSize = 10.5.sp,
                                        color = OceanicLight
                                    )
                                }
                            }

                            Text(
                                text = "Change ➔",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SunsetOrange
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Save Button with Sunset Gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SendButtonGradient)
                        .clickable {
                            val updated = initialConfig.copy(
                                provider = provider,
                                openRouterKey = openRouterKey.trim(),
                                omniRouteKey = omniRouteKey.trim(),
                                openRouterBaseUrl = openRouterBaseUrl.trim(),
                                omniRouteBaseUrl = omniRouteBaseUrl.trim()
                            )
                            onSave(updated)
                            onDismiss()
                        }
                        .testTag("save_settings_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Save & Close",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
