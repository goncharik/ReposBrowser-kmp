package com.honcharenko.reposbrowser.data.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.honcharenko.reposbrowser.data.graphql.GetRepositoryDetailsQuery
import com.honcharenko.reposbrowser.data.graphql.SearchRepositoriesQuery

/**
 * Client for interacting with the GitHub GraphQL API.
 * Wraps Apollo client and provides type-safe methods for executing queries.
 */
class GitHubApiClient(private val apolloClient: ApolloClient) {

    /**
     * Search for GitHub repositories.
     *
     * @param query Search query string (e.g., "kotlin multiplatform")
     * @param first Number of results to return (default: 20)
     * @param after Cursor for pagination (optional)
     * @return Query response containing search results
     */
    suspend fun searchRepositories(
        query: String,
        first: Int = 20,
        after: String? = null
    ): SearchRepositoriesQuery.Data {
        val response = apolloClient.query(
            SearchRepositoriesQuery(
                query = query,
                first = first,
                after = Optional.presentIfNotNull(after)
            )
        ).execute()

        if (response.hasErrors()) {
            val errors = response.errors?.joinToString { it.message }
            throw Exception("GraphQL error: $errors")
        }

        return response.data ?: throw Exception("No data returned from GitHub API")
    }

    /**
     * Get detailed information about a specific repository.
     *
     * @param owner Repository owner login (e.g., "JetBrains")
     * @param name Repository name (e.g., "kotlin")
     * @return Query response containing repository details
     */
    suspend fun getRepositoryDetails(
        owner: String,
        name: String
    ): GetRepositoryDetailsQuery.Data {
        val response = apolloClient.query(
            GetRepositoryDetailsQuery(
                owner = owner,
                name = name
            )
        ).execute()

        if (response.hasErrors()) {
            val errors = response.errors?.joinToString { it.message }
            throw Exception("GraphQL error: $errors")
        }

        return response.data ?: throw Exception("No data returned from GitHub API")
    }

    /**
     * Closes the Apollo client and releases resources.
     */
    fun close() {
        apolloClient.close()
    }
}
