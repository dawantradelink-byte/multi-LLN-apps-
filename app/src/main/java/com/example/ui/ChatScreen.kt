package com.example.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.RectangleShape
import com.example.data.model.AgentPersona
import com.example.data.model.LayoutThemeOption
import com.example.ui.components.ChatBubble
import com.example.ui.components.ChatDrawerContent
import com.example.ui.components.LayoutOptionDialog
import com.example.ui.components.LivingBackgroundEffect
import com.example.ui.components.ModelSelectorDropdown
import com.example.ui.components.SettingsDialog
import com.example.ui.components.VideoGeneratorDialog
import com.example.ui.components.ViralReelDialog
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.CloningGradient
import com.example.ui.theme.CrimsonRose
import com.example.ui.theme.EmptyTextGradient
import com.example.ui.theme.GlowOrange
import com.example.ui.theme.GlowPink
import com.example.ui.theme.HeaderGradient
import com.example.ui.theme.InputBorderGradient
import com.example.ui.theme.NewsreaderFontFamily
import com.example.ui.theme.OceanicGreen
import com.example.ui.theme.OceanicLight
import com.example.ui.theme.PinkishRose
import com.example.ui.theme.SendButtonGradient
import com.example.ui.theme.SunsetOrange
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.messages.collectAsState()
    val input by viewModel.input.collectAsState()
    val isStreaming by viewModel.isStreaming.collectAsState()
    val isCloning by viewModel.isCloning.collectAsState()
    val isRenderingVideo by viewModel.isRenderingVideo.collectAsState()
    val isDeepThink by viewModel.isDeepThink.collectAsState()
    val activePersona by viewModel.activePersona.collectAsState()
    val attachedImageUri by viewModel.attachedImageUri.collectAsState()
    val showVideoDialog by viewModel.showVideoDialog.collectAsState()
    val showViralReelDialog by viewModel.showViralReelDialog.collectAsState()
    val layoutTheme by viewModel.layoutTheme.collectAsState()
    val showLayoutPicker by viewModel.showLayoutPicker.collectAsState()
    val config by viewModel.config.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val currentSessionId by viewModel.currentSessionId.collectAsState()
    val showSettings by viewModel.showSettings.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var showActionSheet by remember { mutableStateOf(false) }

    // Zero-permission Android Photo Picker for Upload option
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setAttachedImage(uri.toString())
        }
    }

    // Auto-scroll when messages update, cloning, or video rendering starts
    LaunchedEffect(messages.size, isCloning, isRenderingVideo, isStreaming) {
        val count = messages.size + (if (isCloning || isRenderingVideo) 1 else 0)
        if (count > 0) {
            listState.animateScrollToItem(count - 1)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Living Ambient Background Effect (Framer & MotionSight.ai)
        LivingBackgroundEffect(layoutOption = layoutTheme)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = BackgroundDark.copy(alpha = 0.95f),
                    drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                ) {
                    ChatDrawerContent(
                        sessions = sessions,
                        currentSessionId = currentSessionId,
                        onSessionSelected = { viewModel.selectSession(it) },
                        onNewChat = { viewModel.startNewChat() },
                        onDeleteSession = { viewModel.deleteSession(it) },
                        onOpenSettings = { viewModel.setShowSettings(true) },
                        onCloseDrawer = { scope.launch { drawerState.close() } }
                    )
                }
            }
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color.Transparent,
                topBar = {
                    // Modern Framer / MotionSight Frosted Glass Top Bar
                    val topBarBg = when (layoutTheme) {
                        LayoutThemeOption.FRAMER_GLASS -> Color(0xFF0E0E12).copy(alpha = 0.88f)
                        LayoutThemeOption.MOTIONSIGHT_AURORA -> Color(0xFF101015).copy(alpha = 0.85f)
                        LayoutThemeOption.FRAMER_EDITORIAL -> Color(0xFF131316).copy(alpha = 0.92f)
                    }

                    val topBorderBrush = when (layoutTheme) {
                        LayoutThemeOption.FRAMER_GLASS -> Brush.horizontalGradient(
                            listOf(Color.White.copy(alpha = 0.12f), Color.White.copy(alpha = 0.03f))
                        )
                        LayoutThemeOption.MOTIONSIGHT_AURORA -> Brush.horizontalGradient(
                            listOf(OceanicGreen.copy(alpha = 0.6f), SunsetOrange.copy(alpha = 0.6f), CrimsonRose.copy(alpha = 0.6f))
                        )
                        LayoutThemeOption.FRAMER_EDITORIAL -> Brush.horizontalGradient(
                            listOf(Color(0xFFE5A962).copy(alpha = 0.45f), Color.White.copy(alpha = 0.05f))
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(topBarBg)
                            .border(width = 1.dp, brush = topBorderBrush, shape = RectangleShape)
                            .statusBarsPadding()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left: Drawer Menu & App Brand + Persona Toggle
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { scope.launch { drawerState.open() } },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .testTag("menu_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Menu",
                                        tint = TextWhite.copy(alpha = 0.95f),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))

                                // Parallel Agent Toggle in Title
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            val next = if (activePersona == AgentPersona.DAWAN_CORE) {
                                                AgentPersona.MADISON_AGENCY
                                            } else {
                                                AgentPersona.DAWAN_CORE
                                            }
                                            viewModel.switchPersona(next)
                                        }
                                        .padding(horizontal = 6.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = activePersona.title,
                                                fontSize = 16.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite,
                                                modifier = Modifier.testTag("header_title")
                                            )
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (activePersona == AgentPersona.MADISON_AGENCY) SunsetOrange else CrimsonRose
                                                    )
                                            )
                                        }
                                        Text(
                                            text = activePersona.roleBadge,
                                            fontSize = 9.5.sp,
                                            color = GlowOrange,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.SwapHoriz,
                                        contentDescription = "Switch Agent",
                                        tint = TextWhite.copy(alpha = 0.65f),
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }

                            // Right: Layout Studio Switcher Pill + Model Dropdown + Settings
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Live Layout Switcher Pill Button
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White.copy(alpha = 0.08f))
                                        .border(
                                            1.dp,
                                            when (layoutTheme) {
                                                LayoutThemeOption.FRAMER_GLASS -> Color(0xFFFDACF7).copy(alpha = 0.5f)
                                                LayoutThemeOption.MOTIONSIGHT_AURORA -> SunsetOrange.copy(alpha = 0.6f)
                                                LayoutThemeOption.FRAMER_EDITORIAL -> Color(0xFFE5A962).copy(alpha = 0.6f)
                                            },
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.setShowLayoutPicker(true) }
                                        .padding(horizontal = 8.dp, vertical = 5.dp)
                                        .testTag("layout_style_pill")
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Palette,
                                            contentDescription = "Select Layout",
                                            tint = when (layoutTheme) {
                                                LayoutThemeOption.FRAMER_GLASS -> Color(0xFFFDACF7)
                                                LayoutThemeOption.MOTIONSIGHT_AURORA -> SunsetOrange
                                                LayoutThemeOption.FRAMER_EDITORIAL -> Color(0xFFE5A962)
                                            },
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = when (layoutTheme) {
                                                LayoutThemeOption.FRAMER_GLASS -> "Framer"
                                                LayoutThemeOption.MOTIONSIGHT_AURORA -> "MotionSight"
                                                LayoutThemeOption.FRAMER_EDITORIAL -> "Studio"
                                            },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                ModelSelectorDropdown(
                                    selectedModelId = config.selectedModel,
                                    onModelSelected = { viewModel.onModelSelected(it.id) }
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                IconButton(
                                    onClick = { viewModel.setShowSettings(true) },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("settings_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Settings",
                                        tint = TextWhite.copy(alpha = 0.85f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                },
            bottomBar = {
                // Input Area with Framer / MotionSight Floating Command Dock
                val bottomBarBg = when (layoutTheme) {
                    LayoutThemeOption.FRAMER_GLASS -> Color(0xFF0C0C0F).copy(alpha = 0.90f)
                    LayoutThemeOption.MOTIONSIGHT_AURORA -> Color(0xFF0F0F13).copy(alpha = 0.88f)
                    LayoutThemeOption.FRAMER_EDITORIAL -> Color(0xFF121215).copy(alpha = 0.92f)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bottomBarBg)
                        .navigationBarsPadding()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    // Quick Mode Chips Row
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Parallel Agent Switcher Pill
                        item {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (activePersona == AgentPersona.MADISON_AGENCY) {
                                            Brush.horizontalGradient(listOf(SunsetOrange, CrimsonRose))
                                        } else {
                                            SolidColor(Color(0xFF222222))
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        if (activePersona == AgentPersona.MADISON_AGENCY) SunsetOrange else SurfaceBorder,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        val next = if (activePersona == AgentPersona.DAWAN_CORE) {
                                            AgentPersona.MADISON_AGENCY
                                        } else {
                                            AgentPersona.DAWAN_CORE
                                        }
                                        viewModel.switchPersona(next)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .testTag("agent_persona_toggle")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (activePersona == AgentPersona.MADISON_AGENCY) Icons.Default.Campaign else Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = if (activePersona == AgentPersona.MADISON_AGENCY) Color.White else GlowOrange,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (activePersona == AgentPersona.MADISON_AGENCY) "Ad Agency Mode" else "Switch to Agency",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // Google Veo Video Pill
                        item {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF222222))
                                    .border(1.dp, SunsetOrange.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                                    .clickable { viewModel.setShowVideoDialog(true) }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .testTag("open_video_gen_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Videocam,
                                        contentDescription = "Veo Video",
                                        tint = SunsetOrange,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Google Veo",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextWhite
                                    )
                                }
                            }
                        }

                        // Viral Reels Studio Pill (OpenMontage & MoneyPrinterTurbo)
                        item {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF222222))
                                    .border(1.dp, GlowOrange.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                                    .clickable { viewModel.setShowViralReelDialog(true) }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .testTag("open_viral_reel_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = "Viral Reels",
                                        tint = GlowOrange,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Viral Reels",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextWhite
                                    )
                                }
                            }
                        }

                        // Deep Think Toggle Pill
                        item {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isDeepThink) {
                                            Brush.horizontalGradient(listOf(GlowPink, GlowOrange))
                                        } else {
                                            SolidColor(Color(0xFF222222))
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        if (isDeepThink) GlowOrange else SurfaceBorder,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { viewModel.toggleDeepThink() }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .testTag("deep_think_toggle")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = "Deep Think",
                                        tint = if (isDeepThink) Color(0xFF1A1A1A) else PinkishRose,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isDeepThink) "Deep Think ON" else "Deep Think",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDeepThink) Color(0xFF1A1A1A) else TextWhite
                                    )
                                }
                            }
                        }

                        // Upload Photo Pill
                        item {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF222222))
                                    .border(1.dp, OceanicLight.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                                    .clickable {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .testTag("upload_pill_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        contentDescription = "Upload",
                                        tint = OceanicLight,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Upload Image",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }

                    // Outer glowing gradient container with adaptive theme border
                    val inputBorder = when (layoutTheme) {
                        LayoutThemeOption.MOTIONSIGHT_AURORA -> InputBorderGradient
                        LayoutThemeOption.FRAMER_GLASS -> Brush.horizontalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.28f),
                                Color(0xFFFDACF7).copy(alpha = 0.35f),
                                Color.White.copy(alpha = 0.12f)
                            )
                        )
                        LayoutThemeOption.FRAMER_EDITORIAL -> Brush.horizontalGradient(
                            listOf(
                                Color(0xFFE5A962).copy(alpha = 0.6f),
                                Color(0xFF8B5A2B).copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.1f)
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = if (layoutTheme == LayoutThemeOption.MOTIONSIGHT_AURORA) 8.dp else 4.dp,
                                shape = RoundedCornerShape(26.dp),
                                spotColor = SunsetOrange.copy(alpha = 0.25f)
                            )
                            .clip(RoundedCornerShape(26.dp))
                            .background(inputBorder)
                            .padding(1.5.dp)
                    ) {
                        // Inner dark input container
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.5.dp))
                                .background(Color(0xFF111115))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            // Attached Image Thumbnail Preview inside chat box
                            if (attachedImageUri != null) {
                                Row(
                                    modifier = Modifier
                                        .padding(start = 6.dp, top = 4.dp, bottom = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                                    ) {
                                        AsyncImage(
                                            model = attachedImageUri,
                                            contentDescription = "Attachment preview",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .size(18.dp)
                                                .clip(CircleShape)
                                                .background(Color.Black.copy(alpha = 0.7f))
                                                .clickable { viewModel.clearAttachedImage() },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove attachment",
                                                tint = Color.White,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Photo attached",
                                        fontSize = 11.5.sp,
                                        color = OceanicLight
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                // Attachment Plus Button
                                IconButton(
                                    onClick = { showActionSheet = true },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .testTag("attachment_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add options",
                                        tint = PinkishRose,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                // Text Input Field
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 6.dp, vertical = 8.dp)
                                        .heightIn(min = 24.dp, max = 120.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (input.isEmpty()) {
                                        val placeholder = when {
                                            activePersona == AgentPersona.MADISON_AGENCY -> "Ask MADISON Agency for campaign strategy, Veo commercials, hooks…"
                                            isDeepThink -> "Ask with Deep Think reasoning…"
                                            else -> "Paste GitHub URL or chat…"
                                        }
                                        Text(
                                            text = placeholder,
                                            color = Color(0xFF666666),
                                            fontSize = 14.sp
                                        )
                                    }
                                    BasicTextField(
                                        value = input,
                                        onValueChange = { viewModel.onInputChange(it) },
                                        textStyle = TextStyle(
                                            color = TextWhite,
                                            fontSize = 14.5.sp,
                                            lineHeight = 20.sp
                                        ),
                                        cursorBrush = SolidColor(CrimsonRose),
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            imeAction = ImeAction.Default
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("chat_input_field")
                                    )
                                }

                                // Send Button with Sunset Gradient
                                val canSend = (input.isNotBlank() || attachedImageUri != null) && !isStreaming && !isCloning && !isRenderingVideo
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (canSend) SendButtonGradient else SolidColor(Color(0xFF262626))
                                        )
                                        .clickable(enabled = canSend) {
                                            viewModel.sendMessage()
                                        }
                                        .testTag("send_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = "Send",
                                        tint = if (canSend) Color.White else Color(0xFF666666),
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Disclaimer Caption
                    Text(
                        text = "Google Veo & GitHub cloning require NO key. LLM Chat requires OpenRouter key in Settings.",
                        color = Color(0xFF6A6A6A),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 2.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (messages.isEmpty() && !isCloning && !isRenderingVideo) {
                    // Empty State with Serif Font & Mix & Match Gradients
                    EmptyStateView(
                        activePersona = activePersona,
                        onSelectPrompt = { prompt ->
                            viewModel.sendMessage(prompt)
                        },
                        onOpenVideoGen = { viewModel.setShowVideoDialog(true) },
                        onOpenViralReel = { viewModel.setShowViralReelDialog(true) },
                        onToggleDeepThink = { viewModel.toggleDeepThink() }
                    )
                } else {
                    // Messages List with Spring Scroll Animations
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(messages, key = { it.id }) { message ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn(
                                        animationSpec = spring(
                                            stiffness = Spring.StiffnessMediumLow,
                                            dampingRatio = 0.82f
                                        )
                                    ) + slideInVertically(
                                        initialOffsetY = { 24 },
                                        animationSpec = spring(
                                            stiffness = Spring.StiffnessMediumLow,
                                            dampingRatio = 0.82f
                                        )
                                    ) + scaleIn(
                                        initialScale = 0.96f,
                                        animationSpec = spring(
                                            stiffness = Spring.StiffnessMediumLow,
                                            dampingRatio = 0.82f
                                        )
                                    )
                                ) {
                                    ChatBubble(
                                        message = message,
                                        layoutTheme = layoutTheme,
                                        onRetry = { viewModel.sendMessage(message.content) }
                                    )
                                }
                            }

                            // Claude Code GitHub Cloning Loading Card
                            if (isCloning) {
                                item {
                                    CloningIndicatorCard()
                                }
                            }

                            // Google Veo AI Video Rendering Pipeline Card
                            if (isRenderingVideo) {
                                item {
                                    VeoRenderingIndicatorCard()
                                }
                            }
                        }

                        // Floating Scroll-To-Bottom Indicator with Spring Bounce
                        val showScrollToBottom by remember {
                            derivedStateOf {
                                listState.firstVisibleItemIndex > 1
                            }
                        }

                        AnimatedVisibility(
                            visible = showScrollToBottom,
                            enter = fadeIn() + scaleIn(initialScale = 0.8f),
                            exit = fadeOut() + scaleOut(targetScale = 0.8f),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 12.dp, end = 16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .shadow(elevation = 8.dp, shape = CircleShape, spotColor = SunsetOrange.copy(alpha = 0.4f))
                                    .clip(CircleShape)
                                    .background(Color(0xFF1C1C22))
                                    .border(1.dp, SunsetOrange.copy(alpha = 0.7f), CircleShape)
                                    .clickable {
                                        scope.launch {
                                            listState.animateScrollToItem(messages.size - 1)
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Scroll to bottom",
                                        tint = SunsetOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Latest",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Settings Modal
    if (showSettings) {
        SettingsDialog(
            initialConfig = config,
            onSave = { updated -> viewModel.saveConfig(updated) },
            onDismiss = { viewModel.setShowSettings(false) },
            currentLayoutTheme = layoutTheme,
            onOpenLayoutPicker = { viewModel.setShowLayoutPicker(true) }
        )
    }

    // Google Veo Video Generator Dialog
    if (showVideoDialog) {
        VideoGeneratorDialog(
            onGenerate = { p, s, d, ar, cm, lm ->
                viewModel.generateVideo(
                    prompt = p,
                    style = s,
                    duration = d,
                    aspectRatio = ar,
                    cameraMotion = cm,
                    lightingMood = lm
                )
            },
            onDismiss = { viewModel.setShowVideoDialog(false) }
        )
    }

    // Viral Reels Generator Dialog (OmniRoute & High-Retention Hooks)
    if (showViralReelDialog) {
        ViralReelDialog(
            onGenerate = { topic, template, voice, dur, hookCategory, providerId, simFallback ->
                viewModel.generateViralReel(
                    topic = topic,
                    template = template,
                    voiceover = voice,
                    duration = dur,
                    hookCategory = hookCategory,
                    preferredProviderId = providerId,
                    forceFallbackTest = simFallback
                )
            },
            onDismiss = { viewModel.setShowViralReelDialog(false) }
        )
    }

    // Input 'Plus' Menu Bottom Sheet
    if (showActionSheet) {
        ModalBottomSheet(
            onDismissRequest = { showActionSheet = false },
            containerColor = Color(0xFF1E1E1E)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "STUDIO ACTIONS & AGENTS",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                // 1. Google Veo Video Generation Action
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF262626))
                        .border(1.dp, SunsetOrange.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable {
                            showActionSheet = false
                            viewModel.setShowVideoDialog(true)
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Brush.horizontalGradient(listOf(SunsetOrange, CrimsonRose))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Generate Video (Google Veo)", color = TextWhite, fontSize = 14.5.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SunsetOrange)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text("VEO 2", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text("Create short 4K video clips with cinematic camera motion", color = OceanicLight, fontSize = 11.5.sp)
                    }
                }

                // 2. Viral Reels Studio (OpenMontage + MoneyPrinterTurbo)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF262626))
                        .border(1.dp, GlowOrange.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable {
                            showActionSheet = false
                            viewModel.setShowViralReelDialog(true)
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Brush.horizontalGradient(listOf(GlowOrange, CrimsonRose))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Viral Reels Studio (9:16)", color = TextWhite, fontSize = 14.5.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GlowOrange)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text("OPENMONTAGE + TURBO", color = Color.Black, fontSize = 8.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text("Auto-cut montage, TTS voiceover & Hormozi kinetic captions", color = TextMuted, fontSize = 11.5.sp)
                    }
                }

                // 2. Million-Dollar Ad Agency Agent
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF262626))
                        .border(1.dp, GlowOrange.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .clickable {
                            showActionSheet = false
                            viewModel.switchPersona(AgentPersona.MADISON_AGENCY)
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF332014)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = GlowOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("MADISON Ad Agency Executive", color = TextWhite, fontSize = 14.5.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GlowOrange)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text("PARALLEL AGENT", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text("Multi-million dollar campaigns, viral hooks & Veo storyboards", color = TextMuted, fontSize = 11.5.sp)
                    }
                }

                // 3. Upload Photo / Image
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF262626))
                        .clickable {
                            showActionSheet = false
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        tint = OceanicLight,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text("Upload Photo / Image", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Pick image from device gallery to inspect with AI", color = TextMuted, fontSize = 11.5.sp)
                    }
                }

                // 4. Toggle Deep Think
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF262626))
                        .clickable {
                            showActionSheet = false
                            viewModel.toggleDeepThink()
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = PinkishRose,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            if (isDeepThink) "Disable Deep Think" else "Enable Deep Think",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text("Multi-step reasoning chain with verified proof", color = TextMuted, fontSize = 11.5.sp)
                    }
                }

                // 5. GitHub Repo paste
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF262626))
                        .clickable {
                            showActionSheet = false
                            viewModel.onInputChange("https://github.com/facebook/react")
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = OceanicGreen,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text("Paste Sample GitHub Repo", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Test Claude Code cloning with facebook/react", color = TextMuted, fontSize = 11.5.sp)
                    }
                }

                // 6. Layout & UI Options (Framer & MotionSight)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF262626))
                        .clickable {
                            showActionSheet = false
                            viewModel.setShowLayoutPicker(true)
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = null,
                        tint = SunsetOrange,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text("Change Layout & UI Style", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Choose from Framer Glass, MotionSight Aurora, or Studio", color = TextMuted, fontSize = 11.5.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    // Layout Selector Dialog (Framer & MotionSight 3-option studio)
    if (showLayoutPicker) {
        LayoutOptionDialog(
            selectedTheme = layoutTheme,
            onSelectTheme = { chosen ->
                viewModel.setLayoutTheme(chosen)
            },
            onDismiss = { viewModel.setShowLayoutPicker(false) }
        )
    }
}
}

@Composable
private fun EmptyStateView(
    activePersona: AgentPersona,
    onSelectPrompt: (String) -> Unit,
    onOpenVideoGen: () -> Unit,
    onOpenViralReel: () -> Unit,
    onToggleDeepThink: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (activePersona == AgentPersona.MADISON_AGENCY) {
            // Million Dollar Ad Agency Empty State
            Icon(
                imageVector = Icons.Default.Campaign,
                contentDescription = "MADISON Agency",
                tint = GlowOrange,
                modifier = Modifier
                    .size(52.dp)
                    .scale(pulseScale)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Welcome to MADISON Agency",
                fontFamily = NewsreaderFontFamily,
                fontSize = 27.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                style = TextStyle(brush = Brush.horizontalGradient(listOf(GlowOrange, CrimsonRose))),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your million-dollar ad agency partner. Creative direction, viral hooks, OpenMontage reels, and Google Veo commercials.",
                color = Color(0xFFAAAAAA),
                fontSize = 13.5.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 14.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SampleChip(
                    icon = Icons.Default.TrendingUp,
                    title = "🔥 Viral Reel Studio (OpenMontage + Turbo)",
                    subtitle = "Automated video montage, TTS voiceover & Hormozi kinetic captions",
                    accentColor = GlowOrange,
                    onClick = onOpenViralReel
                )
                SampleChip(
                    icon = Icons.Default.Movie,
                    title = "🎬 Veo Commercial Script + Video",
                    subtitle = "Scene-by-scene cinematography ready for Google Veo",
                    accentColor = SunsetOrange,
                    onClick = { onSelectPrompt("Write a high-converting 3-scene video commercial script for Google Veo") }
                )
                SampleChip(
                    icon = Icons.Default.Campaign,
                    title = "🚀 Launch 360° Ad Campaign",
                    subtitle = "Positioning, viral hooks, and media channel ROAS model",
                    accentColor = PinkishRose,
                    onClick = { onSelectPrompt("Build an institutional 360-degree ad campaign for our innovative product") }
                )
            }
        } else {
            // Core DAWAN aI Empty State
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "DAWAN aI Sparkle",
                tint = PinkishRose,
                modifier = Modifier
                    .size(48.dp)
                    .scale(pulseScale)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Up late, ishak dawan?",
                fontFamily = NewsreaderFontFamily,
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                style = TextStyle(brush = EmptyTextGradient),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Google Veo video generator, viral reels studio, or million-dollar ad agency partner!",
                color = Color(0xFF888888),
                fontSize = 13.5.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SampleChip(
                    icon = Icons.Default.TrendingUp,
                    title = "🔥 Viral Reel Studio (OpenMontage + Turbo)",
                    subtitle = "Render 9:16 vertical short-form video with kinetic captions",
                    accentColor = GlowOrange,
                    onClick = onOpenViralReel
                )
                SampleChip(
                    icon = Icons.Default.Videocam,
                    title = "🎬 Generate Video with Google Veo",
                    subtitle = "Create 4K cinematic video clips with optical flow",
                    accentColor = SunsetOrange,
                    onClick = onOpenVideoGen
                )
                SampleChip(
                    icon = Icons.Default.Campaign,
                    title = "🏆 MADISON Ad Agency Partner",
                    subtitle = "Activate million-dollar ad executive strategist",
                    accentColor = PinkishRose,
                    onClick = {
                        onSelectPrompt("Act as MADISON Agency: Pitch a million-dollar ad campaign strategy")
                    }
                )
                SampleChip(
                    icon = Icons.Default.Code,
                    title = "https://github.com/facebook/react",
                    subtitle = "Clone & analyze React repo (No API key needed)",
                    accentColor = Color(0xFF5DB8A6),
                    onClick = { onSelectPrompt("https://github.com/facebook/react") }
                )
            }
        }
    }
}

@Composable
private fun SampleChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF14141A).copy(alpha = 0.85f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(13.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF7E7E7E),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun CloningIndicatorCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .shadow(6.dp, RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 4.dp, bottomEnd = 18.dp))
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 4.dp, bottomEnd = 18.dp))
                .background(CloningGradient)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("cloning_indicator")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Cloning GitHub repository…",
                    color = Color.White,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun VeoRenderingIndicatorCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .shadow(6.dp, RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 4.dp, bottomEnd = 18.dp))
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 4.dp, bottomEnd = 18.dp))
                .background(Brush.horizontalGradient(listOf(SunsetOrange, CrimsonRose)))
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("video_rendering_indicator")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Google Veo 2: Synthesizing Diffusion Frames…",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Computing optical flow vectors & 4K neural resolution",
                        color = Color(0xFFFFD5B8),
                        fontSize = 10.5.sp
                    )
                }
            }
        }
    }
}
