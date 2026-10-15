package com.zeeonworks.tinta.ui.archives.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeeonworks.tinta.data.model.DocumentArchive

/**
 * 🍏 Apple Books "Continue" Reading Card (Plek Ketiplek Screenshot 1)
 */
@Composable
fun AppleBooksContinueCard(
    archive: DocumentArchive,
    index: Int,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    val readMinutes = (archive.wordCount / 200).coerceAtLeast(1)
    val simulatedProgress = ((index * 27 + 45) % 85 + 15).coerceIn(10, 99)

    Box(
        modifier = modifier
            .width(280.dp)
            .height(104.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(18.dp), spotColor = Color(0x33000000))
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF2C2C2E)) // Dark slate container
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book cover thumbnail on the left with 3D spine gradient
            Box(
                modifier = Modifier
                    .width(54.dp)
                    .height(80.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = when (index % 4) {
                                0 -> listOf(Color(0xFF38234C), Color(0xFF563673))
                                1 -> listOf(Color(0xFF1B3B4B), Color(0xFF2E5D75))
                                2 -> listOf(Color(0xFF4A3425), Color(0xFF6B4A34))
                                else -> listOf(Color(0xFF223A2B), Color(0xFF365C44))
                            }
                        )
                    )
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = archive.title.take(14),
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 10.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title, Word count/time, and Progress percentage
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = archive.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "$readMinutes min read • ${archive.wordCount} words",
                    fontSize = 11.sp,
                    color = Color(0xFF8E8E93),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$simulatedProgress%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A84FF) // Apple Blue in Dark
                )
            }

            // Three-dot contextual menu button
            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Options",
                        tint = Color(0xFF8E8E93)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Open Dispatch") },
                        onClick = {
                            showMenu = false
                            onClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Remove from Archives", color = Color(0xFFFF453A)) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}
