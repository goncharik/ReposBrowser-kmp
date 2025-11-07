package com.honcharenko.reposbrowser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honcharenko.reposbrowser.data.model.Repository
import com.honcharenko.reposbrowser.data.model.RepositoryDetails
import com.honcharenko.reposbrowser.data.repository.FavoritesRepository
import com.honcharenko.reposbrowser.data.repository.GitHubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Repository Details screen.
 * Manages repository details state, loading state, errors, and favorites.
 */
class RepoDetailsViewModel(
    private val gitHubRepository: GitHubRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    // Repository details data
    private val _repositoryDetails = MutableStateFlow<RepositoryDetails?>(null)
    val repositoryDetails: StateFlow<RepositoryDetails?> = _repositoryDetails.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Favorite state
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    /**
     * Loads repository details from the API
     */
    fun loadDetails(owner: String, name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            gitHubRepository.getRepositoryDetails(owner = owner, name = name)
                .onSuccess { details ->
                    _repositoryDetails.value = details
                    // Check if this repository is favorited
                    checkFavoriteStatus(details.id)
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to load repository details"
                }

            _isLoading.value = false
        }
    }

    /**
     * Checks if the current repository is in favorites
     */
    private suspend fun checkFavoriteStatus(repositoryId: String) {
        _isFavorite.value = favoritesRepository.isFavorite(repositoryId)
    }

    /**
     * Toggles favorite status for the current repository
     */
    fun toggleFavorite() {
        val details = _repositoryDetails.value ?: return

        viewModelScope.launch {
            // Convert RepositoryDetails to Repository for favorites
            val repository = Repository(
                id = details.id,
                name = details.name,
                nameWithOwner = details.nameWithOwner,
                description = details.description,
                stargazersCount = details.stargazersCount,
                forksCount = details.forksCount,
                language = details.primaryLanguage,
                languageColor = details.primaryLanguageColor,
                ownerLogin = details.ownerLogin,
                ownerAvatarUrl = details.ownerAvatarUrl,
                url = details.url
            )

            favoritesRepository.toggleFavorite(repository)
                .onSuccess { isFavorited ->
                    _isFavorite.value = isFavorited
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to update favorite"
                }
        }
    }

    /**
     * Clears the error message
     */
    fun clearError() {
        _error.value = null
    }
}
