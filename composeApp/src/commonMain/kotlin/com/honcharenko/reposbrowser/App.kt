package com.honcharenko.reposbrowser

import androidx.compose.runtime.Composable
import com.honcharenko.reposbrowser.ui.screens.SearchScreen
import com.honcharenko.reposbrowser.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Main application composable.
 * Wraps the SearchScreen with the AppTheme.
 *
 * Note: Navigation is handled natively on each platform:
 * - iOS: SwiftUI NavigationView
 * - Android: Jetpack Compose Navigation
 */
@Composable
@Preview
fun App() {
    AppTheme {
        SearchScreen(
            onRepositoryClick = { repository ->
                // Navigation will be handled by platform-specific code
                // For now, this is a no-op in the shared code
                println("Repository clicked: ${repository.nameWithOwner}")
            }
        )
    }
}