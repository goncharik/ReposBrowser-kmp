package com.honcharenko.reposbrowser.data.repository

import com.honcharenko.reposbrowser.data.local.FavoritesDatabase
import com.honcharenko.reposbrowser.data.model.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of FavoritesRepository using SQLDelight local database.
 * Maps database entities to domain models.
 */
class FavoritesRepositoryImpl(
    private val database: FavoritesDatabase
) : FavoritesRepository {

    override fun getAllFavorites(): Flow<List<Repository>> {
        return database.getAllFavoritesFlow().map { favorites ->
            favorites.map { favorite ->
                Repository(
                    id = favorite.id,
                    name = favorite.name,
                    nameWithOwner = "${favorite.ownerLogin}/${favorite.name}",
                    description = favorite.description,
                    stargazersCount = favorite.stargazersCount.toInt(),
                    forksCount = favorite.forksCount.toInt(),
                    language = favorite.language,
                    languageColor = null, // Not stored in database
                    ownerLogin = favorite.ownerLogin,
                    ownerAvatarUrl = favorite.ownerAvatarUrl,
                    url = favorite.url
                )
            }
        }
    }

    override suspend fun getFavorites(): List<Repository> {
        return database.getAllFavorites().map { favorite ->
            Repository(
                id = favorite.id,
                name = favorite.name,
                nameWithOwner = "${favorite.ownerLogin}/${favorite.name}",
                description = favorite.description,
                stargazersCount = favorite.stargazersCount.toInt(),
                forksCount = favorite.forksCount.toInt(),
                language = favorite.language,
                languageColor = null,
                ownerLogin = favorite.ownerLogin,
                ownerAvatarUrl = favorite.ownerAvatarUrl,
                url = favorite.url
            )
        }
    }

    override suspend fun isFavorite(repositoryId: String): Boolean {
        return database.isFavorite(repositoryId)
    }

    override fun isFavoriteFlow(repositoryId: String): Flow<Boolean> {
        return database.isFavoriteFlow(repositoryId)
    }

    override suspend fun addFavorite(repository: Repository): Result<Unit> {
        return try {
            database.insertOrReplaceFavorite(
                id = repository.id,
                name = repository.name,
                ownerLogin = repository.ownerLogin,
                ownerAvatarUrl = repository.ownerAvatarUrl,
                description = repository.description,
                stargazersCount = repository.stargazersCount.toLong(),
                forksCount = repository.forksCount.toLong(),
                language = repository.language,
                url = repository.url
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFavorite(repositoryId: String): Result<Unit> {
        return try {
            database.deleteFavorite(repositoryId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleFavorite(repository: Repository): Result<Boolean> {
        return try {
            val isFavorited = isFavorite(repository.id)
            if (isFavorited) {
                removeFavorite(repository.id)
                Result.success(false)
            } else {
                addFavorite(repository)
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFavoritesCount(): Long {
        return database.getFavoritesCount()
    }

    override fun getFavoritesCountFlow(): Flow<Long> {
        return database.getFavoritesCountFlow()
    }
}
