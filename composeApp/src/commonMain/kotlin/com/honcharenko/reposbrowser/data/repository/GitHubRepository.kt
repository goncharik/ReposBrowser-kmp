package com.honcharenko.reposbrowser.data.repository

import com.honcharenko.reposbrowser.data.model.Repository
import com.honcharenko.reposbrowser.data.model.RepositoryDetails

/**
 * Repository interface for GitHub API operations.
 * Abstracts the data source and provides a clean API for the domain layer.
 */
interface GitHubRepository {

    /**
     * Search for GitHub repositories.
     *
     * @param query Search query string
     * @param limit Number of results to return
     * @param after Cursor for pagination
     * @return Pair of repositories list and hasNextPage flag
     */
    suspend fun searchRepositories(
        query: String,
        limit: Int = 20,
        after: String? = null
    ): Result<Pair<List<Repository>, Boolean>>

    /**
     * Get detailed information about a specific repository.
     *
     * @param owner Repository owner login
     * @param name Repository name
     * @return Repository details
     */
    suspend fun getRepositoryDetails(
        owner: String,
        name: String
    ): Result<RepositoryDetails>
}
