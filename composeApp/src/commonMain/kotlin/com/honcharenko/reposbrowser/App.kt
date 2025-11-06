package com.honcharenko.reposbrowser

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
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
    // Set up Coil's ImageLoader
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .crossfade(true)
            .build()
    }

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