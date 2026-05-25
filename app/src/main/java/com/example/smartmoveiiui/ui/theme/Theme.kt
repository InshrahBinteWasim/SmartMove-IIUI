package com.example.smartmoveiiui.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = PrimaryGreen,
    secondary = PrimaryGreenLight,
    tertiary = AccentMint,

    background = BackgroundLight,
    surface = SurfaceLight,

    onPrimary = SurfaceLight,
    onSecondary = SurfaceLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight
)

private val DarkColors = darkColorScheme(
    primary = GlowGreen,
    secondary = AccentMint,
    tertiary = WarningAmber,

    background = BackgroundDark,
    surface = SurfaceDark,

    onPrimary = BackgroundDark,
    onSecondary = BackgroundDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)

@Composable
fun SmartMoveIIUITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}