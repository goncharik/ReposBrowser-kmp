package com.honcharenko.reposbrowser.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.honcharenko.reposbrowser.data.model.Repository
import com.honcharenko.reposbrowser.ui.components.LoadingIndicator
import com.honcharenko.reposbrowser.ui.components.RepositoryCard
import com.honcharenko.reposbrowser.viewmodel.FavoritesViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Favorites screen displaying all saved repositories.
 *
 * Features:
 * - Shows list of favorited repositories
 * - Empty state when no favorites exist
 * - Click repository to view details
 * - Toggle favorite to remove from list
 * - Automatic updates when favorites change
 */
@Composable
fun FavoritesScreen(
    onRepositoryClick: (Repository) -> Unit = {},
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val favorites by viewModel.favorites.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Show error in snackbar
    LaunchedEffect(error) {
        error?.let { errorMessage ->
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            // Loading state (rarely shown as Flow is instant)
            isLoading && favorites.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingIndicator(message = "Loading favorites...")
                }
            }

            // Empty state
            favorites.isEmpty() -> {
                EmptyFavoritesState(modifier = Modifier.fillMaxSize())
            }

            // Favorites list
            else -> {
                FavoritesList(
                    favorites = favorites,
                    onRepositoryClick = onRepositoryClick,
                    onFavoriteClick = viewModel::toggleFavorite,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Snackbar host at bottom
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        )
    }
}

/** Empty state when no favorites exist */
@Composable
private fun EmptyFavoritesState(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            // Star icon
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )

            // Title
            Text(
                text = "No favorites yet",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = "Tap the ★ on repositories to save them here",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** List of favorited repositories */
@Composable
private fun FavoritesList(
    favorites: List<Repository>,
    onRepositoryClick: (Repository) -> Unit,
    onFavoriteClick: (Repository) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = favorites, key = { it.id }) { repository ->
            RepositoryCard(
                repository = repository,
                isFavorite = true, // All items in favorites are favorited
                onFavoriteClick = { onFavoriteClick(repository) },
                onClick = { onRepositoryClick(repository) }
            )
        }

        // Spacer at the end for better UX
        item { Spacer(modifier = Modifier.height(58.dp)) }
    }
}
