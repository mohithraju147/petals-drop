package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightFloralColorScheme = lightColorScheme(
    primary = PetalPrimary,
    secondary = PetalSecondary,
    tertiary = PetalTertiary,
    background = PetalBackground,
    surface = PetalSurface,
    onPrimary = Color.White,
    onSecondary = PetalTextDark,
    onTertiary = SleekDarkPinkText,
    onBackground = PetalTextDark,
    onSurface = PetalTextDark,
    surfaceVariant = PetalCard,
    onSurfaceVariant = SleekMutedText,
    outline = SleekOutline
)

private val HighContrastColorScheme = lightColorScheme(
    primary = HighContrastPrimary,
    secondary = HighContrastPrimary,
    tertiary = HighContrastPrimary,
    background = HighContrastBg,
    surface = HighContrastSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = HighContrastTextDark,
    onSurface = HighContrastTextDark,
    surfaceVariant = HighContrastCard,
    onSurfaceVariant = HighContrastTextDark
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    // Both states are soft floral or super readable high-contrast
    val colorScheme = if (isHighContrast) {
        HighContrastColorScheme
    } else {
        LightFloralColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
