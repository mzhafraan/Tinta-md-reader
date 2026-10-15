package com.zeeonworks.tinta.ui.archives

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zeeonworks.tinta.data.model.DocumentArchive
import com.zeeonworks.tinta.ui.archives.components.AppleBooksBookCard
import com.zeeonworks.tinta.ui.archives.components.AppleBooksBottomBar
import com.zeeonworks.tinta.ui.archives.components.AppleBooksContinueCard
import com.zeeonworks.tinta.ui.archives.components.AppleBooksTopPicksCard
import com.zeeonworks.tinta.ui.archives.components.AppleNavTab
import com.zeeonworks.tinta.ui.archives.components.AppleReadingGoalsSheet
import com.zeeonworks.tinta.ui.archives.components.EmptyArchivesView

/**
 * 🍏 Apple Books Home & Library Experience (Plek Ketiplek Screenshot 1, 3, & 4)
 */
@Composable
fun ArchivesScreen(
    onNavigateToReader: (String) -> Unit,
    viewModel: ArchivesViewModel = viewModel()
) {
    val archives by viewModel.archives.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var currentTab by remember { mutableStateOf(AppleNavTab.HOME) }
    var showGoalsSheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // SAF Document Picker Contract (FR-01)
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

    // Open goals sheet automatically when Goals tab is tapped
    LaunchedEffect(currentTab) {
        if (currentTab == AppleNavTab.GOALS) {
            showGoalsSheet = true
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AppleBooksBottomBar(
                currentTab = currentTab,
                onTabSelected = { selected ->
                    currentTab = selected
                }
            )
        },
        floatingActionButton = {
            // Elegant Apple Pill Button for adding dispatches
            ExtendedFloatingActionButton(
                onClick = { filePickerLauncher.launch(arrayOf("*/*")) },
                containerColor = Color(0xFF111111),
                contentColor = Color.White,
                shape = RoundedCornerShape(24.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Import Markdown",
                        tint = Color.White
                    )
                },
                text = {
                    Text(
                        text = "Import Dispatch",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                },
                modifier = Modifier.padding(bottom = 60.dp) // Offset above floating bottom bar
            )
        },
        containerColor = Color(0xFFF9F9FB) // Apple Books Clean Neutral Canvas
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppleNavTab.HOME -> {
                    AppleHomeTabContent(
                        archives = archives,
                        isLoading = uiState.isLoading,
                        onOpenArchive = { archive ->
                            onNavigateToReader(archive.id.ifBlank { archive.uriString })
                        },
                        onDeleteArchive = { archive ->
                            viewModel.deleteArchive(archive)
                        },
                        onOpenGoals = { showGoalsSheet = true },
                        onPickFile = { filePickerLauncher.launch(arrayOf("*/*")) }
                    )
                }
                AppleNavTab.LIBRARY -> {
                    AppleLibraryTabContent(
                        archives = archives,
                        isLoading = uiState.isLoading,
                        onOpenArchive = { archive ->
                            onNavigateToReader(archive.id.ifBlank { archive.uriString })
                        },
                        onPickFile = { filePickerLauncher.launch(arrayOf("*/*")) }
                    )
                }
                AppleNavTab.GOALS -> {
                    // Fallback to Home behind the sheet
                    AppleHomeTabContent(
                        archives = archives,
                        isLoading = uiState.isLoading,
                        onOpenArchive = { archive ->
                            onNavigateToReader(archive.id.ifBlank { archive.uriString })
                        },
                        onDeleteArchive = { archive ->
                            viewModel.deleteArchive(archive)
                        },
                        onOpenGoals = { showGoalsSheet = true },
                        onPickFile = { filePickerLauncher.launch(arrayOf("*/*")) }
                    )
                }
                AppleNavTab.SEARCH -> {
                    AppleSearchTabContent(
                        archives = archives,
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onOpenArchive = { archive ->
                            onNavigateToReader(archive.id.ifBlank { archive.uriString })
                        }
                    )
                }
            }

            // Goals Modal Sheet (Screenshot 4)
            if (showGoalsSheet) {
                AppleReadingGoalsSheet(
                    completedCount = minOf(archives.size, 5),
                    monthlyGoal = 5,
                    recentArchives = archives,
                    onDismissRequest = {
                        showGoalsSheet = false
                        if (currentTab == AppleNavTab.GOALS) {
                            currentTab = AppleNavTab.HOME
                        }
                    }
                )
            }
        }
    }
}

/**
 * 🍏 Tab 1: Apple Books "Home" Tab (Screenshot 1)
 */
@Composable
private fun AppleHomeTabContent(
    archives: List<DocumentArchive>,
    isLoading: Boolean,
    onOpenArchive: (DocumentArchive) -> Unit,
    onDeleteArchive: (DocumentArchive) -> Unit,
    onOpenGoals: () -> Unit,
    onPickFile: () -> Unit
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF007AFF))
        }
        return
    }

    if (archives.isEmpty()) {
        EmptyArchivesView(onProcureDispatch = onPickFile)
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Header Row: "Home" Title & Circular Goal Ring + Profile
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Home",
                    fontSize = 34.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Reading Goal Circular Badge (Screenshot 1)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .shadow(elevation = 2.dp, shape = CircleShape)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable(onClick = onOpenGoals),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${minOf(archives.size, 5)}\n5",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00C7BE), // Apple Cyan
                            lineHeight = 10.sp,
                            modifier = Modifier.padding(2.dp)
                        )
                    }

                    // Avatar Circle
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5E5EA))
                            .clickable(onClick = onOpenGoals),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color(0xFF6E6E73),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Section 1: "Continue" Horizontal Row (Screenshot 1)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Continue",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    itemsIndexed(archives) { index, archive ->
                        AppleBooksContinueCard(
                            archive = archive,
                            index = index,
                            onClick = { onOpenArchive(archive) },
                            onDelete = { onDeleteArchive(archive) }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Section 2: "Top Picks" (Screenshot 1)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Top Picks",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111),
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                AppleBooksTopPicksCard(
                    archives = archives,
                    onSelectArchive = onOpenArchive
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section 3: "All Dispatches / Want to Read" (Screenshot 1)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "All Dispatches",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "View all",
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(14.dp)
                    )
                }

                Text(
                    text = "${archives.size} files",
                    fontSize = 13.sp,
                    color = Color(0xFF8E8E93)
                )
            }
        }

        // Shelf of books
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                itemsIndexed(archives) { index, archive ->
                    AppleBooksBookCard(
                        archive = archive,
                        index = index,
                        onClick = { onOpenArchive(archive) },
                        modifier = Modifier.width(130.dp)
                    )
                }
            }
        }
    }
}

/**
 * 🍏 Tab 2: Apple Books "Library" Tab (Screenshot 3)
 */
@Composable
private fun AppleLibraryTabContent(
    archives: List<DocumentArchive>,
    isLoading: Boolean,
    onOpenArchive: (DocumentArchive) -> Unit,
    onPickFile: () -> Unit
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF007AFF))
        }
        return
    }

    if (archives.isEmpty()) {
        EmptyArchivesView(onProcureDispatch = onPickFile)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Library",
            fontSize = 34.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111111)
        )

        Text(
            text = "${archives.size} offline documents in your collection",
            fontSize = 13.sp,
            color = Color(0xFF8E8E93)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 90.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(archives) { index, archive ->
                AppleBooksBookCard(
                    archive = archive,
                    index = index,
                    onClick = { onOpenArchive(archive) }
                )
            }
        }
    }
}

/**
 * 🍏 Tab 4: Apple Books Search Filter
 */
@Composable
private fun AppleSearchTabContent(
    archives: List<DocumentArchive>,
    query: String,
    onQueryChange: (String) -> Unit,
    onOpenArchive: (DocumentArchive) -> Unit
) {
    val filtered = remember(query, archives) {
        if (query.isBlank()) archives
        else archives.filter {
            it.title.contains(query, ignoreCase = true) || it.snippet.contains(query, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Search",
            fontSize = 34.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111111)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Apple Style Search Input Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFE5E5EA))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF8E8E93),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = {
                        Text(
                            text = "Search titles, notes, and specs...",
                            fontSize = 15.sp,
                            color = Color(0xFF8E8E93)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                if (query.isNotBlank()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = Color(0xFF8E8E93)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (query.isBlank()) "Recent Dispatches" else "${filtered.size} Results Found",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6E6E73)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 90.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(filtered) { index, archive ->
                AppleBooksBookCard(
                    archive = archive,
                    index = index,
                    onClick = { onOpenArchive(archive) }
                )
            }
        }
    }
}
