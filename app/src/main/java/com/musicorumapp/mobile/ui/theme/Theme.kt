package com.musicorumapp.mobile.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AppMaterialIcons = Icons.Rounded

//
//fun createThemeFromColor(color: Color): ColorScheme {
//    var hsl = composeColorToHSL(color)
//    hsl = HSL(hsl.h, 100, 95)
//    var rgb = hsl.toRGB()
//
//    val onPrimary = Color(rgb.r, rgb.g, rgb.b)
//
//    hsl = HSL(hsl.h, 73, 36)
//    rgb = hsl.toRGB()
//    val primary = Color(rgb.r, rgb.g, rgb.b)
//    return darkColorScheme(
//
//        primary = primary,
//        onPrimary = onPrimary,
//        primaryContainer = primary,
//
//        secondary = primary,
//        onSecondary = onPrimary,
//        secondaryContainer = primary,
//
//        tertiary = primary,
//        onTertiary = onPrimary,
//        tertiaryContainer = primary,
//
//        error = md_theme_dark_error,
//        errorContainer = md_theme_dark_errorContainer,
//        onError = md_theme_dark_onError,
//        onErrorContainer = md_theme_dark_onErrorContainer,
//        background = md_theme_dark_background,
//        onBackground = md_theme_dark_onBackground,
//        surface = md_theme_dark_surface,
//        onSurface = md_theme_dark_onSurface,
//        surfaceVariant = md_theme_dark_surfaceVariant,
//        onSurfaceVariant = md_theme_dark_onSurfaceVariant,
//        outline = md_theme_dark_outline,
//        inverseOnSurface = md_theme_dark_inverseOnSurface,
//        inverseSurface = md_theme_dark_inverseSurface,
//        inversePrimary = md_theme_dark_inversePrimary,
////	shadow = md_theme_dark_shadow,
//    )
//}



val DarkThemeColors = darkColorScheme(

    primary = MostlyRed,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = MostlyRed,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,

    secondary = SecondaryTextColor,
    onSecondary = md_theme_dark_onPrimary,
    secondaryContainer = MostlyRed,
    onSecondaryContainer = md_theme_dark_onPrimaryContainer,

    tertiary = MostlyRed,
    onTertiary = md_theme_dark_onPrimary,
    tertiaryContainer = MostlyRed,
    onTertiaryContainer = md_theme_dark_onPrimaryContainer,

    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
//	shadow = md_theme_dark_shadow,
)

@Composable
fun MusicorumTheme(content: @Composable() () -> Unit) {
    MaterialTheme(
        typography = AppTypography,
        content = content,
        colorScheme = DarkThemeColors
    )
}