package com.zeeonworks.tinta.ui.reader

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zeeonworks.tinta.ui.reader.components.AppleThemesBottomSheet
import com.zeeonworks.tinta.ui.reader.components.EditorialDivider
import com.zeeonworks.tinta.ui.reader.components.MarkdownContent
import com.zeeonworks.tinta.ui.theme.AppleBooksThemeProvider
import com.zeeonworks.tinta.ui.theme.AppleBooksThemePreset
import com.zeeonworks.tinta.ui.theme.AppleBooksThemeState

/**
 * 🍏 Apple Books Reading Canvas & "Themes & Settings" Integration (Plek Ketiplek Screenshot 2)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    archiveId: String,
    onBack: () -> Unit,
    viewModel: ReaderViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Apple Books Live Reading Settings State
    var themeState by remember {
        mutableStateOf(AppleBooksThemeState(preset = AppleBooksThemePreset.CALM)) // Defaults to Calm (Screenshot 2)
    }
    var showThemesSheet by remember { mutableStateOf(false) }

    LaunchedEffect(archiveId) {
        viewModel.loadDocument(archiveId)
    }

    val currentColors = themeState.colors
    val backgroundColor = currentColors.background

    AppleBooksThemeProvider(themeState = themeState) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Muted Book/Document Title (Screenshot 2)
                            Text(
                                text = uiState.title.ifBlank { "Tinta Dispatch" },
                                fontSize = 13.sp,
                                fontFamily = themeState.font.fontFamily,
                                fontWeight = FontWeight.Normal,
                                color = currentColors.textMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Return to Library",
                                tint = currentColors.textPrimary
                            )
                        }
                    },
                    actions = {
                        // "Aa" Theme & Settings trigger button (Screenshot 2)
                        Box(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(currentColors.surface)
                                .clickable { showThemesSheet = true }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aa",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = currentColors.textPrimary
                            )
                        }

                        // Share Action
                        if (uiState.content.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, uiState.content)
                                        putExtra(Intent.EXTRA_TITLE, uiState.title)
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "Share Dispatch")
                                    context.startActivity(shareIntent)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = "Share Dispatch",
                                    tint = currentColors.textPrimary
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = backgroundColor
                    )
                )
            },
            containerColor = backgroundColor
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(backgroundColor)
            ) {
                when {
                    uiState.isLoading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = currentColors.accent,
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Preparing pages in ${themeState.preset.title}...",
                                fontSize = 13.sp,
                                color = currentColors.textMuted
                            )
                        }
                    }

                    uiState.errorMessage != null && uiState.isMissingFile -> {
                        AlertDialog(
                            onDismissRequest = { /* Prevent dismissal */ },
                            title = {
                                Text(
                                    text = "Dispatch Missing",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            text = {
                                Text(
                                    text = uiState.errorMessage ?: "The file cannot be accessed."
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        viewModel.removeMissingFileFromArchives {
                                            onBack()
                                        }
                                    }
                                ) {
                                    Text("Remove from Library", color = Color(0xFFFF3B30))
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = onBack) {
                                    Text("Return")
                                }
                            }
                        )
                    }

                    uiState.errorMessage != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Unable to Open Dispatch",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = currentColors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.errorMessage ?: "An unexpected error occurred.",
                                fontSize = 14.sp,
                                color = currentColors.textMuted,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            TextButton(onClick = onBack) {
                                Text("Return to Library", color = currentColors.accent)
                            }
                        }
                    }

                    else -> {
                        // Main Apple Books Reading Canvas (Screenshot 2)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    // Tap page to show/hide Themes & Settings
                                    showThemesSheet = true
                                }
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        ) {
                            // Centered Editorial Title (Screenshot 2)
                            Text(
                                text = uiState.title,
                                fontSize = 28.sp,
                                fontFamily = themeState.font.fontFamily,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = currentColors.textPrimary,
                                lineHeight = 34.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp)
                            )

                            // Date / Subhead Metadata (Screenshot 2 style)
                            if (uiState.dateStamp.isNotBlank()) {
                                Text(
                                    text = uiState.dateStamp,
                                    fontSize = 14.sp,
                                    fontFamily = themeState.font.fontFamily,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    textAlign = TextAlign.Center,
                                    color = currentColors.textSecondary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 6.dp)
                                )
                            }

                            Text(
                                text = "${uiState.wordCount} words • ~${uiState.readingTimeMinutes} min read",
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                color = currentColors.textMuted,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )

                            EditorialDivider(ornament = "• • •")

                            Spacer(modifier = Modifier.height(10.dp))

                            // Fluid Markdown Content
                            MarkdownContent(
                                markdown = uiState.content,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(48.dp))

                            EditorialDivider(ornament = "— ❦ —")

                            Spacer(modifier = Modifier.height(60.dp))
                        }

                        // Floating Quick "Aa" Pill Button on bottom right for easy one-handed access
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 20.dp, end = 20.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .shadow(elevation = 8.dp, shape = CircleShape)
                                    .clip(CircleShape)
                                    .background(currentColors.surface)
                                    .clickable { showThemesSheet = true }
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Aa",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    color = currentColors.textPrimary
                                )
                            }
                        }
                    }
                }

                // Apple Books Themes & Settings Modal Bottom Sheet (Screenshot 2)
                if (showThemesSheet) {
                    AppleThemesBottomSheet(
                        themeState = themeState,
                        onThemeStateChange = { updated ->
                            themeState = updated
                        },
                        onDismissRequest = {
                            showThemesSheet = false
                        }
                    )
                }
            }
        }
    }
}
