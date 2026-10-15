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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeeonworks.tinta.data.model.DocumentArchive

/**
 * 🍏 Apple Books "Top Picks" Showcase Card (Plek Ketiplek Screenshot 1)
 */
@Composable
fun AppleBooksTopPicksCard(
    archives: List<DocumentArchive>,
    onSelectArchive: (DocumentArchive) -> Unit,
    modifier: Modifier = Modifier
) {
    val topItems = archives.take(3)
    val firstItem = topItems.firstOrNull()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(22.dp), spotColor = Color(0x22000000))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .clickable {
                firstItem?.let { onSelectArchive(it) }
            }
            .padding(18.dp)
    ) {
        Column {
            // Standing 3D Book Covers Layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                // Main Highlight Book Cover
                Box(
                    modifier = Modifier
                        .width(96.dp)
                        .height(134.dp)
                        .shadow(elevation = 12.dp, shape = RoundedCornerShape(6.dp))
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF1F2937), Color(0xFF111827))
                            )
                        )
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = firstItem?.title?.take(22) ?: "PRD & SPEC",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 14.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "MARKDOWN",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF59E0B),
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Secondary Companion Books Stack
                Column(
                    modifier = Modifier.height(134.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .width(68.dp)
                            .height(62.dp)
                            .shadow(elevation = 6.dp, shape = RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF0F766E), Color(0xFF115E59))
                                )
                            )
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = topItems.getOrNull(1)?.title?.take(14) ?: "Notes",
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(68.dp)
                            .height(62.dp)
                            .shadow(elevation = 6.dp, shape = RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFFB45309), Color(0xFF92400E))
                                )
                            )
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = topItems.getOrNull(2)?.title?.take(14) ?: "Dispatch",
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Text Info at the bottom of the card
            Text(
                text = "Dispatches You Might Like",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Suggestions based on your recent reading and local archives.",
                fontSize = 13.sp,
                color = Color(0xFF6E6E73),
                lineHeight = 17.sp
            )
        }
    }
}
