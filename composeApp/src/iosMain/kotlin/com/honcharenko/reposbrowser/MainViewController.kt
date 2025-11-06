package com.honcharenko.reposbrowser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.honcharenko.reposbrowser.di.initKoin
import com.honcharenko.reposbrowser.ui.screens.SearchScreen
import com.honcharenko.reposbrowser.ui.theme.AppTheme

/**
 * Initializes Koin for iOS.
 * Should be called once from Swift/UIKit before using the MainViewController.
 */
fun initKoinIos() {
    initKoin()
}

/**
 * Main view controller for iOS (legacy - kept for compatibility).
 * Creates a Compose UIViewController with the App composable.
 */
fun MainViewController() = ComposeUIViewController { App() }

/**
 * Search screen view controller for iOS.
 * Used within SwiftUI TabView navigation.
 */
fun SearchViewController() = ComposeUIViewController {
    AppTheme {
        SearchScreen(
            onRepositoryClick = { repository ->
                // TODO: Navigate to repository details screen when implemented
                // Navigation will be handled by SwiftUI NavigationStack
                println("Repository clicked: ${repository.nameWithOwner}")
            }
        )
    }
}

/**
 * Favorites screen view controller for iOS.
 * Placeholder until FavoritesScreen is implemented in Phase 3.
 */
fun FavoritesViewController() = ComposeUIViewController {
    AppTheme {
        FavoritesPlaceholder()
    }
}

/**
 * Placeholder composable for Favorites screen
 */
@Composable
private fun FavoritesPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Favorites Screen - Coming Soon",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}