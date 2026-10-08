package com.zeeonworks.tinta.ui.archives

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zeeonworks.tinta.data.model.DocumentArchive
import com.zeeonworks.tinta.ui.archives.components.ArchiveCard
import com.zeeonworks.tinta.ui.archives.components.EmptyArchivesView
import com.zeeonworks.tinta.ui.theme.TintaBorderDark
import com.zeeonworks.tinta.ui.theme.TintaBorderHairline
import com.zeeonworks.tinta.ui.theme.TintaCharcoalInk
import com.zeeonworks.tinta.ui.theme.TintaMutedInk
import com.zeeonworks.tinta.ui.theme.TintaPaperCard
import com.zeeonworks.tinta.ui.theme.TintaParchmentBackground
import com.zeeonworks.tinta.ui.theme.TintaTerracotta
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ArchivesScreen(
    onNavigateToReader: (String) -> Unit,
    viewModel: ArchivesViewModel = viewModel()
) {
    val archives by viewModel.archives.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var archiveToDelete by remember { mutableStateOf<DocumentArchive?>(null) }

    // File Picker Contract (FR-01: OpenDocument)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onDocumentSelected(uri, onNavigateToReader)
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.infoMessage) {
        uiState.infoMessage?.let { info ->
            snackbarHostState.showSnackbar(info)
            viewModel.clearMessages()
        }
    }

    val todayStamp = remember {
        val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US)
        sdf.format(Date()).uppercase()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    filePickerLauncher.launch(arrayOf("*/*"))
                },
                containerColor = TintaTerracotta,
                contentColor = TintaPaperCard,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(2.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Clipping",
                        tint = TintaPaperCard
                    )
                },
                text = {
                    Text(
                        text = "ADD CLIPPING",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = TintaPaperCard
                    )
                }
            )
        },
        containerColor = TintaParchmentBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Masthead / Vintage Editorial Header
            EditorialMasthead(todayStamp = todayStamp)

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = TintaTerracotta,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Unfolding dispatch...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TintaMutedInk
                        )
                    }
                }
            } else if (archives.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyArchivesView(
                        onProcureDispatch = {
                            filePickerLauncher.launch(arrayOf("*/*"))
                        }
                    )
                }
            } else {
                // 2-Column Newspaper Clippings Grid (FR-05)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 80.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = archives,
                        key = { it.id.ifBlank { it.uriString } }
                    ) { archive ->
                        ArchiveCard(
                            archive = archive,
                            onClick = {
                                onNavigateToReader(archive.id)
                            },
                            onDelete = {
                                archiveToDelete = archive
                            }
                        )
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog (FR-04)
    archiveToDelete?.let { archive ->
        AlertDialog(
            onDismissRequest = { archiveToDelete = null },
            title = {
                Text(
                    text = "Remove Clipping?",
                    style = MaterialTheme.typography.titleMedium,
                    color = TintaCharcoalInk
                )
            },
            text = {
                Text(
                    text = "Remove \"${archive.title}\" from The Archives? The original dispatch file on your device will remain untouched.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TintaMutedInk
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteArchive(archive)
                        archiveToDelete = null
                    }
                ) {
                    Text(
                        text = "REMOVE",
                        style = MaterialTheme.typography.labelMedium,
                        color = TintaTerracotta
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { archiveToDelete = null }) {
                    Text(
                        text = "CANCEL",
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

@Composable
private fun EditorialMasthead(todayStamp: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Double hairline rule on top
        HorizontalDivider(thickness = 1.dp, color = TintaBorderDark)
        Spacer(modifier = Modifier.height(2.dp))
        HorizontalDivider(thickness = 0.5.dp, color = TintaBorderHairline)
        Spacer(modifier = Modifier.height(8.dp))

        // Main Masthead Title
        Text(
            text = "THE ARCHIVES",
            style = MaterialTheme.typography.displayLarge.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            ),
            color = TintaCharcoalInk,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Subtitle banner / Issue stamp
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = todayStamp,
                style = MaterialTheme.typography.labelSmall,
                color = TintaMutedInk
            )

            Text(
                text = "VINTAGE DISPATCH READER",
                style = MaterialTheme.typography.labelSmall,
                color = TintaTerracotta
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.5.dp, color = TintaCharcoalInk)
        Spacer(modifier = Modifier.height(1.dp))
        HorizontalDivider(thickness = 0.5.dp, color = TintaBorderHairline)
    }
}
