package com.zeeonworks.tinta.ui.archives.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeeonworks.tinta.ui.theme.TintaBorderHairline
import com.zeeonworks.tinta.ui.theme.TintaCharcoalInk
import com.zeeonworks.tinta.ui.theme.TintaMutedInk
import com.zeeonworks.tinta.ui.theme.TintaPaperCard
import com.zeeonworks.tinta.ui.theme.TintaTerracotta

@Composable
fun EmptyArchivesView(
    onProcureDispatch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(TintaPaperCard, RoundedCornerShape(4.dp))
                .border(BorderStroke(1.dp, TintaBorderHairline), RoundedCornerShape(4.dp))
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Vintage Emblem / Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(TintaBorderHairline.copy(alpha = 0.25f), RoundedCornerShape(32.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoStories,
                    contentDescription = null,
                    tint = TintaTerracotta,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Poetic Headline
            Text(
                text = "NO CLIPPINGS IN REPOSITORY",
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.2.sp),
                color = TintaMutedInk,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Poetic Empty State Message (PRD FR-07)
            Text(
                text = "\"The Archives are presently empty. Procure a dispatch to begin reading.\"",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontStyle = FontStyle.Italic,
                    lineHeight = 24.sp
                ),
                color = TintaCharcoalInk,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap below to import a Markdown (.md) or plain text dispatch via the system file archive.",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = TintaMutedInk,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Editorial Action Button
            Button(
                onClick = onProcureDispatch,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TintaTerracotta,
                    contentColor = TintaPaperCard
                ),
                shape = RoundedCornerShape(2.dp),
                border = BorderStroke(1.dp, TintaBorderHairline)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.NoteAdd,
                    contentDescription = null,
                    tint = TintaPaperCard,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "PROCURE DISPATCH",
                    style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 0.8.sp),
                    color = TintaPaperCard
                )
            }
        }
    }
}
