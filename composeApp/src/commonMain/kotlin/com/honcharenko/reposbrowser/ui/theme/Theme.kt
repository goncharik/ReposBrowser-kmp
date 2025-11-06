package com.honcharenko.reposbrowser.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Light color scheme based on GitHub's design
 */
private val LightColorScheme = lightColorScheme(
    primary = GitHubBlue,
    onPrimary = GitHubBackground,
    primaryContainer = GitHubSurface,
    onPrimaryContainer = GitHubBlueDark,
    secondary = GitHubGray,
    onSecondary = GitHubBackground,
    secondaryContainer = GitHubSurface,
    onSecondaryContainer = GitHubGrayLight,
    tertiary = GitHubGreen,
    onTertiary = GitHubBackground,
    error = GitHubRed,
    onError = GitHubBackground,
    background = GitHubBackground,
    onBackground = GitHubGray,
    surface = GitHubSurface,
    onSurface = GitHubGray,
    surfaceVariant = GitHubSurface,
    onSurfaceVariant = GitHubGrayLight,
    outline = GitHubBorder,
    outlineVariant = GitHubBorder
)

/**
 * Dark color scheme based on GitHub's dark theme
 */
private val DarkColorScheme = darkColorScheme(
    primary = GitHubBlueDarkTheme,
    onPrimary = GitHubBackgroundDark,
    primaryContainer = GitHubSurfaceDark,
    onPrimaryContainer = GitHubBlueLightDarkTheme,
    secondary = GitHubGrayDarkTheme,
    onSecondary = GitHubBackgroundDark,
    secondaryContainer = GitHubSurfaceDark,
    onSecondaryContainer = GitHubGrayLightDarkTheme,
    tertiary = GitHubGreenDark,
    onTertiary = GitHubBackgroundDark,
    error = GitHubRedDark,
    onError = GitHubBackgroundDark,
    background = GitHubBackgroundDark,
    onBackground = GitHubGrayDarkTheme,
    surface = GitHubSurfaceDark,
    onSurface = GitHubGrayDarkTheme,
    surfaceVariant = GitHubSurfaceDark,
    onSurfaceVariant = GitHubGrayLightDarkTheme,
    outline = GitHubBorderDark,
    outlineVariant = GitHubBorderDark
)

/**
 * Main app theme.
 * Applies Material3 theming with GitHub-inspired colors.
 *
 * @param darkTheme Whether to use dark theme (defaults to system setting)
 * @param content The composable content
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
