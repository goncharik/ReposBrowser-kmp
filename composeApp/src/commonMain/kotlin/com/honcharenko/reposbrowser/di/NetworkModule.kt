package com.honcharenko.reposbrowser.di

import com.honcharenko.reposbrowser.data.api.ApolloClientFactory
import com.honcharenko.reposbrowser.data.api.GitHubApiClient
import org.koin.dsl.module

/**
 * Koin module for network-related dependencies.
 * Provides Apollo client and GitHub API client.
 */
val networkModule = module {
    // Apollo Client - Singleton
    single { ApolloClientFactory.create() }

    // GitHub API Client - Singleton
    single { GitHubApiClient(apolloClient = get()) }
}
