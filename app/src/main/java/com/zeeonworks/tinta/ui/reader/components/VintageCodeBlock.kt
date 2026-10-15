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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeeonworks.tinta.ui.theme.LocalAppleBooksTheme
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
    val theme = LocalAppleBooksTheme.current

    val displayLang = language?.trim()?.uppercase()?.ifBlank { "CODE" } ?: "CODE"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(theme.colors.codeBackground)
            .border(BorderStroke(0.5.dp, theme.colors.border), RoundedCornerShape(8.dp))
    ) {
        // Top Bar: Language Badge & Copy Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.colors.surface)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayLang,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = theme.colors.textMuted
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isCopied) {
                    Text(
                        text = "COPIED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.colors.accent
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Copied",
                        tint = theme.colors.accent,
                        modifier = Modifier.size(14.dp)
                    )
                } else {
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Code Dispatch", code)
                            clipboard.setPrimaryClip(clip)

                            isCopied = true
                            onCopied?.invoke()

                            Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show()

                            coroutineScope.launch {
                                delay(2000)
                                isCopied = false
                            }
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "Copy code block",
                            tint = theme.colors.textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Code Body with horizontal scroll
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(horizontalScrollState)
                .padding(14.dp)
        ) {
            Text(
                text = code.trimEnd(),
                fontSize = (theme.fontSizeSp - 3f).coerceAtLeast(11f).sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = (theme.fontSizeSp * 1.3f).sp,
                color = theme.colors.codeText
            )
        }
    }
}
