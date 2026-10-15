package com.zeeonworks.tinta.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 🍏 Apple Books 6 Canonical Reading Themes (Matching Apple Books iOS)
 */
enum class AppleBooksThemePreset(
    val title: String,
    val previewBg: Color,
    val previewText: Color,
    val isDark: Boolean = false,
    val isBoldText: Boolean = false
) {
    ORIGINAL(
        title = "Original",
        previewBg = Color(0xFFFFFFFF),
        previewText = Color(0xFF111111)
    ),
    QUIET(
        title = "Quiet",
        previewBg = Color(0xFF242426),
        previewText = Color(0xFFE5E5EA),
        isDark = true
    ),
    PAPER(
        title = "Paper",
        previewBg = Color(0xFFF6F4EB),
        previewText = Color(0xFF2C2A29)
    ),
    BOLD(
        title = "Bold",
        previewBg = Color(0xFFFFFFFF),
        previewText = Color(0xFF000000),
        isBoldText = true
    ),
    CALM(
        title = "Calm", // Signature Apple Books Warm Tan (Screenshot 2)
        previewBg = Color(0xFFEFE8D8),
        previewText = Color(0xFF3D342A)
    ),
    FOCUS(
        title = "Focus",
        previewBg = Color(0xFFFAF6EE),
        previewText = Color(0xFF242220)
    );

    val colors: AppleColors
        get() = when (this) {
            ORIGINAL -> AppleColors(
                background = Color(0xFFFFFFFF),
                surface = Color(0xFFF2F2F7),
                card = Color(0xFFFFFFFF),
                textPrimary = Color(0xFF111111),
                textSecondary = Color(0xFF6E6E73),
                textMuted = Color(0xFF8E8E93),
                border = Color(0xFFE5E5EA),
                codeBackground = Color(0xFFF2F2F7),
                codeText = Color(0xFF111111),
                accent = Color(0xFF007AFF), // Apple Blue
                isDark = false
            )
            QUIET -> AppleColors(
                background = Color(0xFF1C1C1E),
                surface = Color(0xFF2C2C2E),
                card = Color(0xFF2C2C2E),
                textPrimary = Color(0xFFF2F2F7),
                textSecondary = Color(0xFFA1A1A6),
                textMuted = Color(0xFF636366),
                border = Color(0xFF38383A),
                codeBackground = Color(0xFF2C2C2E),
                codeText = Color(0xFFE5E5EA),
                accent = Color(0xFF0A84FF),
                isDark = true
            )
            PAPER -> AppleColors(
                background = Color(0xFFF8F5EE),
                surface = Color(0xFFEFECE2),
                card = Color(0xFFFCFAF4),
                textPrimary = Color(0xFF23211E),
                textSecondary = Color(0xFF706B62),
                textMuted = Color(0xFF948E84),
                border = Color(0xFFDBD6C9),
                codeBackground = Color(0xFFEFECE2),
                codeText = Color(0xFF23211E),
                accent = Color(0xFFB8860B),
                isDark = false
            )
            BOLD -> AppleColors(
                background = Color(0xFFFFFFFF),
                surface = Color(0xFFF4F4F6),
                card = Color(0xFFFFFFFF),
                textPrimary = Color(0xFF000000),
                textSecondary = Color(0xFF3A3A3C),
                textMuted = Color(0xFF6C6C70),
                border = Color(0xFFD1D1D6),
                codeBackground = Color(0xFFE5E5EA),
                codeText = Color(0xFF000000),
                accent = Color(0xFF007AFF),
                isDark = false
            )
            CALM -> AppleColors(
                background = Color(0xFFEFE8D8),
                surface = Color(0xFFE5DDCB),
                card = Color(0xFFF7F3EA),
                textPrimary = Color(0xFF3D342A),
                textSecondary = Color(0xFF7D6F5E),
                textMuted = Color(0xFF9E8F7C),
                border = Color(0xFFD5CBB8),
                codeBackground = Color(0xFFE2D9C5),
                codeText = Color(0xFF2E261E),
                accent = Color(0xFF9E6539), // Warm Sienna
                isDark = false
            )
            FOCUS -> AppleColors(
                background = Color(0xFFFAF6EE),
                surface = Color(0xFFF0EBDE),
                card = Color(0xFFFFFFFF),
                textPrimary = Color(0xFF242220),
                textSecondary = Color(0xFF746F68),
                textMuted = Color(0xFF99938B),
                border = Color(0xFFE0D9CB),
                codeBackground = Color(0xFFEFECE2),
                codeText = Color(0xFF242220),
                accent = Color(0xFF5B7065), // Sage Green
                isDark = false
            )
        }
}

enum class AppleBooksFont(val displayName: String, val fontFamily: FontFamily) {
    SERIF("New York (Serif)", FontFamily.Serif),
    SANS("San Francisco (Sans)", FontFamily.SansSerif),
    MONO("Typewriter (Mono)", FontFamily.Monospace)
}

data class AppleColors(
    val background: Color,
    val surface: Color,
    val card: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val border: Color,
    val codeBackground: Color,
    val codeText: Color,
    val accent: Color,
    val isDark: Boolean
)

data class AppleBooksThemeState(
    val preset: AppleBooksThemePreset = AppleBooksThemePreset.CALM,
    val font: AppleBooksFont = AppleBooksFont.SERIF,
    val fontSizeSp: Float = 17f,
    val lineSpacingMultiplier: Float = 1.5f,
    val brightnessMultiplier: Float = 1.0f
) {
    val colors: AppleColors get() = preset.colors
    val fontWeight: FontWeight get() = if (preset.isBoldText) FontWeight.SemiBold else FontWeight.Normal
}

val LocalAppleBooksTheme = compositionLocalOf { AppleBooksThemeState() }

@Composable
fun AppleBooksThemeProvider(
    themeState: AppleBooksThemeState,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalAppleBooksTheme provides themeState) {
        content()
    }
}
