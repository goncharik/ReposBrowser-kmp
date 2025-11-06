package com.honcharenko.reposbrowser.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.honcharenko.reposbrowser.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Wrapper around SQLDelight database for managing favorite repositories.
 * Provides suspend functions and Flow-based reactive queries.
 */
class FavoritesDatabase(
    private val driver: app.cash.sqldelight.db.SqlDriver
) {
    private val database = ReposBrowserDatabase(driver)
    private val queries = database.favoritesQueries

    /**
     * Get all favorites as a Flow that emits updates when data changes.
     */
    fun getAllFavoritesFlow(): Flow<List<Favorite>> {
        return queries.selectAll()
            .asFlow()
            .mapToList<Favorite>(Dispatchers.IO)
    }

    /**
     * Get all favorites as a one-time query.
     */
    suspend fun getAllFavorites(): List<Favorite> = withContext(Dispatchers.IO) {
        queries.selectAll().executeAsList()
    }

    /**
     * Get a specific favorite by repository ID.
     */
    suspend fun getFavoriteById(id: String): Favorite? = withContext(Dispatchers.IO) {
        queries.selectById(id).executeAsOneOrNull()
    }

    /**
     * Check if a repository is favorited.
     */
    suspend fun isFavorite(id: String): Boolean = withContext(Dispatchers.IO) {
        queries.isFavorite(id).executeAsOne()
    }

    /**
     * Get favorite status as a Flow.
     */
    fun isFavoriteFlow(id: String): Flow<Boolean> {
        return queries.isFavorite(id)
            .asFlow()
            .mapToOne<Boolean>(Dispatchers.IO)
    }

    /**
     * Add or update a favorite repository.
     */
    suspend fun insertOrReplaceFavorite(
        id: String,
        name: String,
        ownerLogin: String,
        ownerAvatarUrl: String?,
        description: String?,
        stargazersCount: Long,
        forksCount: Long,
        language: String?,
        url: String,
        createdAt: Long = System.currentTimeMillis()
    ): Unit = withContext(Dispatchers.IO) {
        queries.insertOrReplace(
            id = id,
            name = name,
            ownerLogin = ownerLogin,
            ownerAvatarUrl = ownerAvatarUrl,
            description = description,
            stargazersCount = stargazersCount,
            forksCount = forksCount,
            language = language,
            url = url,
            createdAt = createdAt
        )
    }

    /**
     * Remove a favorite by repository ID.
     */
    suspend fun deleteFavorite(id: String): Unit = withContext(Dispatchers.IO) {
        queries.deleteById(id)
    }

    /**
     * Remove all favorites.
     */
    suspend fun deleteAllFavorites(): Unit = withContext(Dispatchers.IO) {
        queries.deleteAll()
    }

    /**
     * Get the total count of favorites.
     */
    suspend fun getFavoritesCount(): Long = withContext(Dispatchers.IO) {
        queries.countAll().executeAsOne()
    }

    /**
     * Get favorites count as a Flow.
     */
    fun getFavoritesCountFlow(): Flow<Long> {
        return queries.countAll()
            .asFlow()
            .mapToOne<Long>(Dispatchers.IO)
    }
}
