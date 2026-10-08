package com.zeeonworks.tinta.ui.reader

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zeeonworks.tinta.ui.reader.components.EditorialDivider
import com.zeeonworks.tinta.ui.reader.components.MarkdownContent
import com.zeeonworks.tinta.ui.theme.TintaBorderHairline
import com.zeeonworks.tinta.ui.theme.TintaCharcoalInk
import com.zeeonworks.tinta.ui.theme.TintaMutedInk
import com.zeeonworks.tinta.ui.theme.TintaPaperCard
import com.zeeonworks.tinta.ui.theme.TintaParchmentBackground
import com.zeeonworks.tinta.ui.theme.TintaTerracotta
import java.util.Locale

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

    LaunchedEffect(archiveId) {
        viewModel.loadDocument(archiveId)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = uiState.title.ifBlank { "THE GAZETTE" },
                                style = MaterialTheme.typography.titleMedium,
                                color = TintaCharcoalInk,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "THE GAZETTE • EDITORIAL DISPATCH",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = TintaMutedInk
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Return to Archives",
                                tint = TintaCharcoalInk
                            )
                        }
                    },
                    actions = {
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
                                    tint = TintaCharcoalInk
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = TintaParchmentBackground
                    )
                )
                HorizontalDivider(thickness = 1.dp, color = TintaBorderHairline)
            }
        },
        containerColor = TintaParchmentBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = TintaTerracotta,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Typesetting dispatch...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TintaMutedInk
                        )
                    }
                }

                uiState.errorMessage != null && !uiState.isMissingFile -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "UNREADABLE DISPATCH",
                            style = MaterialTheme.typography.labelMedium,
                            color = TintaTerracotta
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.errorMessage ?: "Unknown error occurred.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = TintaCharcoalInk
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        TextButton(onClick = onBack) {
                            Text(
                                text = "RETURN TO ARCHIVES",
                                style = MaterialTheme.typography.labelMedium,
                                color = TintaTerracotta
                            )
                        }
                    }
                }

                else -> {
                    // Main Article Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        // Document Headline
                        Text(
                            text = uiState.title,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 26.sp,
                                lineHeight = 32.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = TintaCharcoalInk
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Metadata byline banner
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiState.dateStamp.uppercase(Locale.US),
                                style = MaterialTheme.typography.labelSmall,
                                color = TintaMutedInk
                            )

                            Text(
                                text = "${String.format(Locale.US, "%,d", uiState.wordCount)} WORDS • ${uiState.readingTimeMinutes} MIN READ",
                                style = MaterialTheme.typography.labelSmall,
                                color = TintaTerracotta
                            )
                        }

                        EditorialDivider(ornament = "✦ ✦ ✦")

                        // Rendered Markdown
                        MarkdownContent(markdown = uiState.content)

                        Spacer(modifier = Modifier.height(32.dp))

                        // Newspaper End-of-Article Seal
                        EditorialDivider(ornament = "— ❦ —")
                        Text(
                            text = "FINIS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 3.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = TintaMutedInk,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                }
            }
        }
    }

    // Missing File Dialog (Edge Case FR 6.3)
    if (uiState.isMissingFile) {
        AlertDialog(
            onDismissRequest = onBack,
            title = {
                Text(
                    text = "Dispatch Unavailable",
                    style = MaterialTheme.typography.titleMedium,
                    color = TintaCharcoalInk
                )
            },
            text = {
                Text(
                    text = "The source file could not be found or access has expired (it may have been moved or deleted from the device). Would you like to remove this clipping from The Archives?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TintaMutedInk
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.removeMissingFileFromArchives(onBack)
                    }
                ) {
                    Text(
                        text = "REMOVE FROM ARCHIVES",
                        style = MaterialTheme.typography.labelMedium,
                        color = TintaTerracotta
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onBack) {
                    Text(
                        text = "RETURN",
                        style = MaterialTheme.typography.labelMedium,
                        color = TintaMutedInk
                    )
                }
            },
            containerColor = TintaPaperCard,
            shape = RoundedCornerShape(2.dp)
        )
    }
}
