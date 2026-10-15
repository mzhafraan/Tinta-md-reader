package com.zeeonworks.tinta.ui.archives.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeeonworks.tinta.data.model.DocumentArchive

/**
 * 🍏 Apple Books Stylized Book Card for Library Grid
 */
@Composable
fun AppleBooksBookCard(
    archive: DocumentArchive,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientColors = when (index % 5) {
        0 -> listOf(Color(0xFF1E293B), Color(0xFF0F172A))
        1 -> listOf(Color(0xFF3B2D54), Color(0xFF241B35))
        2 -> listOf(Color(0xFF134E4A), Color(0xFF042F2E))
        3 -> listOf(Color(0xFF78350F), Color(0xFF451A03))
        else -> listOf(Color(0xFF1E3A8A), Color(0xFF172554))
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        // 3D Realistic Book Cover
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(6.dp), spotColor = Color(0x33000000))
                .clip(RoundedCornerShape(6.dp))
                .background(Brush.verticalGradient(gradientColors))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = archive.title,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${archive.wordCount} words",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title Below Book
        Text(
            text = archive.title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF111111),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${(archive.wordCount / 200).coerceAtLeast(1)} min read",
            fontSize = 11.sp,
            color = Color(0xFF8E8E93)
        )
    }
}
