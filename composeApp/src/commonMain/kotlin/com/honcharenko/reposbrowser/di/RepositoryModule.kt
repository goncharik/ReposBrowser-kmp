package com.honcharenko.reposbrowser.di

import com.honcharenko.reposbrowser.data.repository.FavoritesRepository
import com.honcharenko.reposbrowser.data.repository.FavoritesRepositoryImpl
import com.honcharenko.reposbrowser.data.repository.GitHubRepository
import com.honcharenko.reposbrowser.data.repository.GitHubRepositoryImpl
import org.koin.dsl.module

/**
 * Koin module for repository layer dependencies.
 * Provides repository implementations.
 */
val repositoryModule = module {
    // GitHub Repository - Singleton
    single<GitHubRepository> {
        GitHubRepositoryImpl(apiClient = get())
    }

    // Favorites Repository - Singleton
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(database = get())
    }
}
