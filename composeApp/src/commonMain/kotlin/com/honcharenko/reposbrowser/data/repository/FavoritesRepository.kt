package com.honcharenko.reposbrowser.data.repository

import com.honcharenko.reposbrowser.data.model.Repository
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing favorite repositories.
 * Provides access to local storage for favorites.
 */
interface FavoritesRepository {

    /**
     * Get all favorite repositories as a Flow.
     * Emits updates whenever favorites change.
     */
    fun getAllFavorites(): Flow<List<Repository>>

    /**
     * Get all favorite repositories as a one-time query.
     */
    suspend fun getFavorites(): List<Repository>

    /**
     * Check if a repository is favorited.
     */
    suspend fun isFavorite(repositoryId: String): Boolean

    /**
     * Get favorite status as a Flow.
     */
    fun isFavoriteFlow(repositoryId: String): Flow<Boolean>

    /**
     * Add a repository to favorites.
     */
    suspend fun addFavorite(repository: Repository): Result<Unit>

    /**
     * Remove a repository from favorites.
     */
    suspend fun removeFavorite(repositoryId: String): Result<Unit>

    /**
     * Toggle favorite status for a repository.
     * If favorited, removes it. If not favorited, adds it.
     */
    suspend fun toggleFavorite(repository: Repository): Result<Boolean>

    /**
     * Get the total count of favorites.
     */
    suspend fun getFavoritesCount(): Long

    /**
     * Get favorites count as a Flow.
     */
    fun getFavoritesCountFlow(): Flow<Long>
}
