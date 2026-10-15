package com.zeeonworks.tinta.ui.reader.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeeonworks.tinta.ui.theme.LocalAppleBooksTheme

@Composable
fun EditorialDivider(
    modifier: Modifier = Modifier,
    ornament: String? = "✦ ✦ ✦"
) {
    val theme = LocalAppleBooksTheme.current

    if (ornament != null) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = theme.colors.border
            )

            Text(
                text = ornament,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = theme.colors.accent,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = theme.colors.border
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            HorizontalDivider(thickness = 1.dp, color = theme.colors.border)
            Spacer(modifier = Modifier.height(2.dp))
            HorizontalDivider(thickness = 0.5.dp, color = theme.colors.border.copy(alpha = 0.5f))
        }
    }
}
