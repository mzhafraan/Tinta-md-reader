package com.zeeonworks.tinta.ui.reader.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeeonworks.tinta.ui.theme.TintaBorderHairline
import com.zeeonworks.tinta.ui.theme.TintaCodeBackground
import com.zeeonworks.tinta.ui.theme.TintaCodeText
import com.zeeonworks.tinta.ui.theme.TintaMutedInk
import com.zeeonworks.tinta.ui.theme.TintaTagBackground
import com.zeeonworks.tinta.ui.theme.TintaTerracotta
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VintageCodeBlock(
    code: String,
    language: String? = null,
    onCopied: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isCopied by remember { mutableStateOf(false) }
    val horizontalScrollState = rememberScrollState()

    val displayLang = language?.trim()?.uppercase()?.ifBlank { "TYPEWRITER / CODE" } ?: "TYPEWRITER / CODE"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(3.dp))
            .background(TintaCodeBackground)
            .border(BorderStroke(1.dp, TintaBorderHairline), RoundedCornerShape(3.dp))
    ) {
        // Top Bar: Language Badge & Copy Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TintaTagBackground.copy(alpha = 0.5f))
                .border(BorderStroke(0.5.dp, TintaBorderHairline))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayLang,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    letterSpacing = 0.8.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = TintaMutedInk
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isCopied) {
                    Text(
                        text = "COPIED",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TintaTerracotta
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Tinta Code Snippet", code)
                        clipboard.setPrimaryClip(clip)

                        isCopied = true
                        Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show()
                        onCopied?.invoke()

                        coroutineScope.launch {
                            delay(2000)
                            isCopied = false
                        }
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isCopied) Icons.Outlined.Check else Icons.Outlined.ContentCopy,
                        contentDescription = "Copy Code",
                        tint = if (isCopied) TintaTerracotta else TintaMutedInk,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Code Body with horizontal scrolling
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(horizontalScrollState)
                .padding(12.dp)
        ) {
            Text(
                text = code.trimEnd(),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                ),
                color = TintaCodeText
            )
        }
    }
}
