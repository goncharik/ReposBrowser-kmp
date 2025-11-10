package com.honcharenko.reposbrowser.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.honcharenko.reposbrowser.data.model.Repository
import com.honcharenko.reposbrowser.ui.components.LoadingIndicator
import com.honcharenko.reposbrowser.ui.components.RepositoryCard
import com.honcharenko.reposbrowser.viewmodel.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Search screen for browsing GitHub repositories.
 *
 * Features:
 * - Search input with real-time query updates
 * - Repository list with cards
 * - Pagination support (load more)
 * - Favorite management
 * - Loading and error states
 *
 * Note: Navigation bar should be implemented natively on each platform (SwiftUI on iOS, Jetpack
 * Compose on Android)
 */
@Composable
fun SearchScreen(
        viewModel: SearchViewModel = koinViewModel(),
        onRepositoryClick: (Repository) -> Unit = {}
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val repositories by viewModel.repositories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val error by viewModel.error.collectAsState()
    val hasNextPage by viewModel.hasNextPage.collectAsState()
    val favoritesMap by viewModel.favoritesMap.collectAsState()

    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Show error in snackbar
    LaunchedEffect(error) {
        error?.let { errorMessage ->
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.clearError()
        }
    }

    // Detect when user scrolls near the end for pagination
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem != null &&
                    lastVisibleItem.index >= totalItems - 3 &&
                    hasNextPage &&
                    !isLoadingMore &&
                    !isLoading
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadMore()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search input
            SearchBar(
                    query = searchQuery,
                    onQueryChange = viewModel::onSearchQueryChanged,
                    onSearch = {
                        keyboardController?.hide()
                        viewModel.onSearch()
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            // Content area
            when {
                // Initial loading state
                isLoading && repositories.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingIndicator(message = "Searching...")
                    }
                }

                // Empty state
                repositories.isEmpty() && !isLoading && searchQuery.isNotBlank() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                                text = "No repositories found",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Initial state (no search performed)
                repositories.isEmpty() && searchQuery.isBlank() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(16.dp)
                            )
                            Text(
                                    text = "Search for GitHub repositories",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Repository list
                else -> {
                    RepositoryList(
                            repositories = repositories,
                            listState = listState,
                            isLoadingMore = isLoadingMore,
                            hasNextPage = hasNextPage,
                            onRepositoryClick = onRepositoryClick,
                            onFavoriteClick = viewModel::toggleFavorite,
                            isFavorite = { repoId -> favoritesMap[repoId] ?: false },
                            modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Snackbar host at bottom
        SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        )
    }
}

/** Search input bar with clear button. */
@Composable
private fun SearchBar(
        query: String,
        onQueryChange: (String) -> Unit,
        onSearch: () -> Unit,
        modifier: Modifier = Modifier
) {
    TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = modifier,
            placeholder = { Text("Search repositories...") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            colors =
                    TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                    ),
            shape = MaterialTheme.shapes.medium
    )
}

/** Repository list with pagination support. */
@Composable
private fun RepositoryList(
        repositories: List<Repository>,
        listState: androidx.compose.foundation.lazy.LazyListState,
        isLoadingMore: Boolean,
        hasNextPage: Boolean,
        onRepositoryClick: (Repository) -> Unit,
        onFavoriteClick: (Repository) -> Unit,
        isFavorite: (String) -> Boolean,
        modifier: Modifier = Modifier
) {
    LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = repositories, key = { it.id }) { repository ->
            RepositoryCard(
                    repository = repository,
                    isFavorite = isFavorite(repository.id),
                    onFavoriteClick = { onFavoriteClick(repository) },
                    onClick = { onRepositoryClick(repository) }
            )
        }

        // Loading more indicator
        if (isLoadingMore) {
            item {
                Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                ) { LoadingIndicator(message = "Loading more...") }
            }
        }

        // Spacer at the end for better UX
        if (hasNextPage && !isLoadingMore && repositories.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(58.dp)) }
        }
    }
}
