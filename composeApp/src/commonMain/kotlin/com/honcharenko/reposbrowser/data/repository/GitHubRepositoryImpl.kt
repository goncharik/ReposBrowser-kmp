package com.honcharenko.reposbrowser.data.repository

import com.honcharenko.reposbrowser.data.api.GitHubApiClient
import com.honcharenko.reposbrowser.data.model.Repository
import com.honcharenko.reposbrowser.data.model.RepositoryDetails

/**
 * Implementation of GitHubRepository using GitHub GraphQL API.
 * Maps Apollo-generated types to domain models.
 */
class GitHubRepositoryImpl(
    private val apiClient: GitHubApiClient
) : GitHubRepository {

    override suspend fun searchRepositories(
        query: String,
        limit: Int,
        after: String?
    ): Result<Pair<List<Repository>, Boolean>> {
        return try {
            val response = apiClient.searchRepositories(query, limit, after)
            val repositories = response.search.edges?.mapNotNull { edge ->
                val repo = edge?.node?.onRepository ?: return@mapNotNull null
                Repository(
                    id = repo.id,
                    name = repo.name,
                    nameWithOwner = repo.nameWithOwner,
                    description = repo.description,
                    stargazersCount = repo.stargazerCount,
                    forksCount = repo.forkCount,
                    language = repo.primaryLanguage?.name,
                    languageColor = repo.primaryLanguage?.color,
                    ownerLogin = repo.owner.login,
                    ownerAvatarUrl = repo.owner.avatarUrl.toString(),
                    url = repo.url.toString()
                )
            } ?: emptyList()

            val hasNextPage = response.search.pageInfo.hasNextPage
            Result.success(repositories to hasNextPage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRepositoryDetails(
        owner: String,
        name: String
    ): Result<RepositoryDetails> {
        return try {
            val response = apiClient.getRepositoryDetails(owner, name)
            val repo = response.repository ?: throw Exception("Repository not found")

            val details = RepositoryDetails(
                id = repo.id,
                name = repo.name,
                nameWithOwner = repo.nameWithOwner,
                description = repo.description,
                stargazersCount = repo.stargazerCount,
                forksCount = repo.forkCount,
                watchersCount = repo.watchers.totalCount,
                openIssuesCount = repo.issues.totalCount,
                openPullRequestsCount = repo.pullRequests.totalCount,
                primaryLanguage = repo.primaryLanguage?.name,
                primaryLanguageColor = repo.primaryLanguage?.color,
                license = repo.licenseInfo?.name,
                createdAt = repo.createdAt.toString(),
                updatedAt = repo.updatedAt.toString(),
                url = repo.url.toString(),
                homepageUrl = repo.homepageUrl?.toString(),
                ownerLogin = repo.owner.login,
                ownerAvatarUrl = repo.owner.avatarUrl.toString(),
                languages = repo.languages?.edges?.mapNotNull { edge ->
                    val langNode = edge?.node ?: return@mapNotNull null
                    RepositoryDetails.Language(
                        name = langNode.name,
                        color = langNode.color,
                        size = edge.size
                    )
                } ?: emptyList()
            )

            Result.success(details)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
