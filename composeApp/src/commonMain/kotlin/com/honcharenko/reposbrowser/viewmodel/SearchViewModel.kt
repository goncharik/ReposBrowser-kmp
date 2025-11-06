package com.honcharenko.reposbrowser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honcharenko.reposbrowser.data.model.Repository
import com.honcharenko.reposbrowser.data.repository.FavoritesRepository
import com.honcharenko.reposbrowser.data.repository.GitHubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Search screen.
 * Manages search state, repository results, pagination, and favorites.
 */
class SearchViewModel(
    private val gitHubRepository: GitHubRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Repository results
    private val _repositories = MutableStateFlow<List<Repository>>(emptyList())
    val repositories: StateFlow<List<Repository>> = _repositories.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Pagination state
    private val _hasNextPage = MutableStateFlow(false)
    val hasNextPage: StateFlow<Boolean> = _hasNextPage.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    // Real pagination cursor from GitHub GraphQL API
    private var endCursor: String? = null

    /**
     * Updates the search query
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /**
     * Performs a new search with the current query
     */
    fun onSearch() {
        val query = _searchQuery.value.trim()
        if (query.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _repositories.value = emptyList()
            endCursor = null

            gitHubRepository.searchRepositories(
                query = query,
                limit = 20,
                after = null
            ).onSuccess { searchResult ->
                _repositories.value = searchResult.repositories
                _hasNextPage.value = searchResult.hasNextPage
                endCursor = searchResult.endCursor
            }.onFailure { exception ->
                _error.value = exception.message ?: "An error occurred while searching"
            }

            _isLoading.value = false
        }
    }

    /**
     * Loads more repositories (pagination)
     */
    fun loadMore() {
        if (_isLoadingMore.value || !_hasNextPage.value) return

        val query = _searchQuery.value.trim()
        if (query.isEmpty()) return

        viewModelScope.launch {
            _isLoadingMore.value = true

            gitHubRepository.searchRepositories(
                query = query,
                limit = 20,
                after = endCursor
            ).onSuccess { searchResult ->
                _repositories.value = _repositories.value + searchResult.repositories
                _hasNextPage.value = searchResult.hasNextPage
                endCursor = searchResult.endCursor
            }.onFailure { exception ->
                _error.value = exception.message ?: "An error occurred while loading more"
            }

            _isLoadingMore.value = false
        }
    }

    /**
     * Toggles favorite status for a repository
     */
    fun toggleFavorite(repository: Repository) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(repository)
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to update favorite"
                }
        }
    }

    /**
     * Checks if a repository is favorited
     */
    suspend fun isFavorite(repositoryId: String): Boolean {
        return favoritesRepository.isFavorite(repositoryId)
    }

    /**
     * Clears the error message
     */
    fun clearError() {
        _error.value = null
    }
}
