package com.zeeonworks.tinta.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Vintage Editorial Light Color Scheme (Authentic Newsprint / Parchment)
private val TintaEditorialColorScheme = lightColorScheme(
    primary = TintaTerracotta,
    onPrimary = TintaPaperCard,
    primaryContainer = TintaCodeBackground,
    onPrimaryContainer = TintaCharcoalInk,
    secondary = TintaMutedInk,
    onSecondary = TintaPaperCard,
    secondaryContainer = TintaTagBackground,
    onSecondaryContainer = TintaCharcoalInk,
    background = TintaParchmentBackground,
    onBackground = TintaCharcoalInk,
    surface = TintaPaperCard,
    onSurface = TintaCharcoalInk,
    surfaceVariant = TintaCodeBackground,
    onSurfaceVariant = TintaMutedInk,
    outline = TintaBorderHairline,
    outlineVariant = TintaBorderDark,
    error = TintaTerracotta,
    onError = TintaPaperCard
)

@Composable
fun TintaTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = TintaEditorialColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val controller = WindowCompat.getInsetsController(window, view)
                // Use dark icons on top of warm parchment in edge-to-edge mode
                controller.isAppearanceLightStatusBars = true
                controller.isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TintaTypography,
        content = content
    )
}