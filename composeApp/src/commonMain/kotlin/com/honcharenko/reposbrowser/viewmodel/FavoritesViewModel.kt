package com.honcharenko.reposbrowser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honcharenko.reposbrowser.data.model.Repository
import com.honcharenko.reposbrowser.data.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Favorites screen.
 * Manages favorites list state and removal operations.
 */
class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    // Favorites list (reactive via Flow from repository)
    private val _favorites = MutableStateFlow<List<Repository>>(emptyList())
    val favorites: StateFlow<List<Repository>> = _favorites.asStateFlow()

    // Loading state (mainly for initial load, favorites Flow is usually instant)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Derived empty state
    val isEmpty: StateFlow<Boolean> = MutableStateFlow(true).apply {
        viewModelScope.launch {
            favorites.collect { favoritesList ->
                value = favoritesList.isEmpty()
            }
        }
    }

    init {
        // Collect favorites Flow to keep UI updated
        viewModelScope.launch {
            favoritesRepository.getAllFavorites().collect { favoritesList ->
                _favorites.value = favoritesList
            }
        }
    }

    /**
     * Toggles favorite status (removes from favorites)
     */
    fun toggleFavorite(repository: Repository) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(repository)
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to remove favorite"
                }
        }
    }

    /**
     * Removes a repository from favorites
     */
    fun removeFavorite(repository: Repository) {
        viewModelScope.launch {
            favoritesRepository.removeFavorite(repository.id)
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to remove favorite"
                }
        }
    }

    /**
     * Clears all favorites (optional feature)
     */
    fun clearAllFavorites() {
        viewModelScope.launch {
            // Remove each favorite one by one
            val currentFavorites = _favorites.value
            currentFavorites.forEach { repository ->
                favoritesRepository.removeFavorite(repository.id)
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
