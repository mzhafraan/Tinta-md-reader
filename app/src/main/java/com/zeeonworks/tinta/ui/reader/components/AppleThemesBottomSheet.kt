package com.zeeonworks.tinta.ui.reader.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.BrightnessHigh
import androidx.compose.material.icons.outlined.BrightnessLow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewDay
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeeonworks.tinta.ui.theme.AppleBooksFont
import com.zeeonworks.tinta.ui.theme.AppleBooksThemePreset
import com.zeeonworks.tinta.ui.theme.AppleBooksThemeState

/**
 * 🍏 Apple Books "Themes & Settings" Bottom Sheet (Plek Ketiplek Screenshot 2)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppleThemesBottomSheet(
    themeState: AppleBooksThemeState,
    onThemeStateChange: (AppleBooksThemeState) -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isCustomizeExpanded by remember { mutableStateOf(false) }

    val isDark = themeState.preset.isDark
    val sheetBg = if (isDark) Color(0xFF2C2C2E) else Color(0xFFF9F9FA)
    val textPrimary = if (isDark) Color(0xFFFFFFFF) else Color(0xFF111111)
    val textSecondary = if (isDark) Color(0xFF8E8E93) else Color(0xFF6E6E73)
    val controlBg = if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = sheetBg,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Header Row: Title & Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Themes & Settings",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                // Close (X) circle button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(controlBg)
                        .clickable(onClick = onDismissRequest),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Settings",
                        tint = textPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Row 1: Font Size Stepper [ A | A ] & Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Font Size Segmented Capsule [ A | A ]
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(controlBg),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Decrease Font Size (Small A)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(topStart = 22.dp, bottomStart = 22.dp))
                            .clickable {
                                val newSize = (themeState.fontSizeSp - 1.5f).coerceAtLeast(13f)
                                onThemeStateChange(themeState.copy(fontSizeSp = newSize))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = textPrimary
                        )
                    }

                    // Vertical Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(if (isDark) Color(0xFF48484A) else Color(0xFFD1D1D6))
                    )

                    // Increase Font Size (Large A)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(topEnd = 22.dp, bottomEnd = 22.dp))
                            .clickable {
                                val newSize = (themeState.fontSizeSp + 1.5f).coerceAtMost(24f)
                                onThemeStateChange(themeState.copy(fontSizeSp = newSize))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                    }
                }

                // Page / Continuous Scroll Pill
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(controlBg)
                        .clickable {
                            // Cycle font families as quick toggle
                            val nextFont = when (themeState.font) {
                                AppleBooksFont.SERIF -> AppleBooksFont.SANS
                                AppleBooksFont.SANS -> AppleBooksFont.MONO
                                AppleBooksFont.MONO -> AppleBooksFont.SERIF
                            }
                            onThemeStateChange(themeState.copy(font = nextFont))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ViewDay,
                        contentDescription = "Font Style",
                        tint = textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Dark/Light Contrast Quick Toggle
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(controlBg)
                        .clickable {
                            val nextPreset = if (themeState.preset.isDark) {
                                AppleBooksThemePreset.CALM
                            } else {
                                AppleBooksThemePreset.QUIET
                            }
                            onThemeStateChange(themeState.copy(preset = nextPreset))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BrightnessHigh,
                        contentDescription = "Toggle Appearance",
                        tint = textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row 2: Brightness Slider with Sun Icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.BrightnessLow,
                    contentDescription = "Low Brightness",
                    tint = textSecondary,
                    modifier = Modifier.size(18.dp)
                )

                Slider(
                    value = themeState.brightnessMultiplier,
                    onValueChange = { newBrightness ->
                        onThemeStateChange(themeState.copy(brightnessMultiplier = newBrightness))
                    },
                    valueRange = 0.5f..1.0f,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = if (isDark) Color(0xFF636366) else Color(0xFF8E8E93),
                        inactiveTrackColor = controlBg
                    )
                )

                Icon(
                    imageVector = Icons.Outlined.WbSunny,
                    contentDescription = "High Brightness",
                    tint = textSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row 3 & 4: The 6 Apple Books Themes Grid (2 rows x 3 columns)
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val presets = AppleBooksThemePreset.values()

                // Row 1: Original, Quiet, Paper
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (i in 0..2) {
                        AppleThemeCard(
                            preset = presets[i],
                            isSelected = themeState.preset == presets[i],
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onThemeStateChange(themeState.copy(preset = presets[i]))
                            }
                        )
                    }
                }

                // Row 2: Bold, Calm, Focus
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (i in 3..5) {
                        AppleThemeCard(
                            preset = presets[i],
                            isSelected = themeState.preset == presets[i],
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onThemeStateChange(themeState.copy(preset = presets[i]))
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expandable Customize Section
            AnimatedVisibility(
                visible = isCustomizeExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(controlBg)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Font Family",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppleBooksFont.values().forEach { fontOption ->
                            val isSelected = themeState.font == fontOption
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) textPrimary else if (isDark) Color(0xFF2C2C2E) else Color.White
                                    )
                                    .clickable {
                                        onThemeStateChange(themeState.copy(font = fontOption))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (fontOption) {
                                        AppleBooksFont.SERIF -> "Serif"
                                        AppleBooksFont.SANS -> "Sans"
                                        AppleBooksFont.MONO -> "Mono"
                                    },
                                    fontSize = 13.sp,
                                    fontFamily = fontOption.fontFamily,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) sheetBg else textPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Line Spacing",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            1.3f to "Compact",
                            1.5f to "Standard",
                            1.8f to "Relaxed"
                        ).forEach { (multiplier, label) ->
                            val isSelected = themeState.lineSpacingMultiplier == multiplier
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) textPrimary else if (isDark) Color(0xFF2C2C2E) else Color.White
                                    )
                                    .clickable {
                                        onThemeStateChange(themeState.copy(lineSpacingMultiplier = multiplier))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) sheetBg else textPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Capsule Button: "⚙ Customize"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(controlBg)
                    .clickable {
                        isCustomizeExpanded = !isCustomizeExpanded
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Customize",
                        tint = textPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isCustomizeExpanded) "Hide Typography Options" else "Customize",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary
                    )
                }
            }
        }
    }
}

/**
 * Single Apple Theme Preview Card (Matching Screenshot 2)
 */
@Composable
private fun AppleThemeCard(
    preset: AppleBooksThemePreset,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    val borderModifier = if (isSelected) {
        Modifier.border(
            width = 2.dp,
            color = Color(0xFF111111), // Bold dark border when selected
            shape = shape
        )
    } else {
        Modifier.border(
            width = 0.5.dp,
            color = Color(0x33000000),
            shape = shape
        )
    }

    Box(
        modifier = modifier
            .height(76.dp)
            .clip(shape)
            .background(preset.previewBg)
            .then(borderModifier)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Aa",
                fontSize = 19.sp,
                fontWeight = if (preset.isBoldText) FontWeight.Bold else FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                color = preset.previewText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = preset.title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = preset.previewText.copy(alpha = 0.85f)
            )
        }
    }
}
