package com.honcharenko.reposbrowser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.honcharenko.reposbrowser.data.model.Repository
import com.honcharenko.reposbrowser.di.initKoin
import com.honcharenko.reposbrowser.ui.screens.FavoritesScreen
import com.honcharenko.reposbrowser.ui.screens.RepoDetailsScreen
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
 *
 * @param onRepositoryClick Callback invoked when a repository is clicked
 */
fun SearchViewController(onRepositoryClick: (Repository) -> Unit) = ComposeUIViewController {
    AppTheme {
        SearchScreen(
            onRepositoryClick = onRepositoryClick
        )
    }
}

/**
 * Repository details screen view controller for iOS.
 * Used for drill-down navigation from search results.
 *
 * @param repository Repository object to display details for
 */
fun RepoDetailsViewController(repository: Repository) = ComposeUIViewController {
    AppTheme {
        RepoDetailsScreen(
            repository = repository,
            onNavigateBack = { /* SwiftUI handles back navigation */ }
        )
    }
}

/**
 * Favorites screen view controller for iOS.
 * Displays list of favorited repositories.
 *
 * @param onRepositoryClick Callback invoked when a repository is clicked
 */
fun FavoritesViewController(onRepositoryClick: (Repository) -> Unit) = ComposeUIViewController {
    AppTheme {
        FavoritesScreen(
            onRepositoryClick = onRepositoryClick
        )
    }
}