package com.zeeonworks.tinta.ui.reader.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeeonworks.tinta.ui.theme.TintaBorderDark
import com.zeeonworks.tinta.ui.theme.TintaBorderHairline
import com.zeeonworks.tinta.ui.theme.TintaMutedInk
import com.zeeonworks.tinta.ui.theme.TintaTerracotta

@Composable
fun EditorialDivider(
    modifier: Modifier = Modifier,
    ornament: String? = "✦ ✦ ✦"
) {
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
                color = TintaBorderHairline
            )

            Text(
                text = ornament,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    letterSpacing = 2.sp
                ),
                color = TintaTerracotta,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = TintaBorderHairline
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            HorizontalDivider(thickness = 1.dp, color = TintaBorderDark)
            Spacer(modifier = Modifier.height(2.dp))
            HorizontalDivider(thickness = 0.5.dp, color = TintaBorderHairline)
        }
    }
}
