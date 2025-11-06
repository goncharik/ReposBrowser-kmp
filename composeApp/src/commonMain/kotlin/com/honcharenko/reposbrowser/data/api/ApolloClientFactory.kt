package com.honcharenko.reposbrowser.data.api

import com.apollographql.apollo.ApolloClient
import com.honcharenko.reposbrowser.config.Secrets

/**
 * Factory for creating configured Apollo GraphQL client instances.
 * Sets up authentication and network configuration for GitHub GraphQL API.
 */
object ApolloClientFactory {

    private const val GITHUB_API_URL = "https://api.github.com/graphql"

    /**
     * Creates and configures an Apollo client for GitHub GraphQL API.
     * Includes authentication token in request headers.
     */
    fun create(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(GITHUB_API_URL)
            .addHttpHeader("Authorization", "Bearer ${Secrets.GITHUB_TOKEN}")
            .addHttpHeader("Content-Type", "application/json")
            .build()
    }
}
