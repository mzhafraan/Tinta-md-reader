package com.zeeonworks.tinta.ui.archives.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeeonworks.tinta.data.model.DocumentArchive

/**
 * 🍏 Apple Books "Reading Goals" Modal Sheet (Plek Ketiplek Screenshot 4)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppleReadingGoalsSheet(
    completedCount: Int,
    monthlyGoal: Int = 5,
    recentArchives: List<DocumentArchive>,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Close Button (X) in top right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF2F2F7))
                        .clickable(onClick = onDismissRequest),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF3C3C43),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle Title with orange highlight
            Text(
                text = buildAnnotatedString {
                    append("Book time for yourself\nwith ")
                    withStyle(SpanStyle(color = Color(0xFFF26829), fontWeight = FontWeight.Bold)) {
                        append("Reading Goals.")
                    }
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                color = Color(0xFF111111)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Bookshelf with completed badges (Screenshot 4)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                val totalSlots = 5
                val activeItems = recentArchives.take(completedCount)

                for (i in 0 until totalSlots) {
                    val isFinished = i < completedCount
                    val archiveItem = activeItems.getOrNull(i)

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(width = 62.dp, height = 90.dp)
                    ) {
                        if (isFinished) {
                            // Finished Book Cover with 3D gradient
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(84.dp)
                                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(4.dp))
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            colors = when (i % 3) {
                                                0 -> listOf(Color(0xFF1A365D), Color(0xFF2A4365))
                                                1 -> listOf(Color(0xFF234E52), Color(0xFF285E61))
                                                else -> listOf(Color(0xFF5B3219), Color(0xFF744210))
                                            }
                                        )
                                    )
                                    .padding(6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = archiveItem?.title?.take(18) ?: "Dispatch",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 11.sp
                                )
                            }

                            // Blue checkmark badge on bottom-right of book
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.BottomEnd)
                                    .shadow(elevation = 2.dp, shape = CircleShape)
                                    .clip(CircleShape)
                                    .background(Color(0xFF007AFF)), // Apple Blue
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Finished",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        } else {
                            // Empty goal slot with faint number
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(84.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFF2F2F7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${i + 1}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Light,
                                    color = Color(0xFFC7C7CC)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Main Serif Stats Headline
            Text(
                text = "Dispatches Read This Month",
                fontSize = 22.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$completedCount dispatches finished.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007AFF) // Apple Blue
            )

            Spacer(modifier = Modifier.height(8.dp))

            val remaining = (monthlyGoal - completedCount).coerceAtLeast(0)
            Text(
                text = if (remaining > 0) {
                    "Read $remaining more dispatches to reach your goal for this month."
                } else {
                    "Splendid work! You've achieved your reading goal."
                },
                fontSize = 14.sp,
                color = Color(0xFF6E6E73),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Full width black pill button: SHARE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color(0xFF111111))
                    .clickable(onClick = onDismissRequest),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SHARE",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Text Button: ADJUST GOAL
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = "ADJUST GOAL",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = Color(0xFF111111)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
