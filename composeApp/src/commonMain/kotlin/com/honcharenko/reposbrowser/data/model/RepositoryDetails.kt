package com.honcharenko.reposbrowser.data.model

/**
 * Domain model for detailed GitHub repository information.
 * Contains comprehensive data for the repository details screen.
 */
data class RepositoryDetails(
    val id: String,
    val name: String,
    val nameWithOwner: String,
    val description: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val watchersCount: Int,
    val openIssuesCount: Int,
    val openPullRequestsCount: Int,
    val primaryLanguage: String?,
    val primaryLanguageColor: String?,
    val license: String?,
    val createdAt: String,
    val updatedAt: String,
    val url: String,
    val homepageUrl: String?,
    val ownerLogin: String,
    val ownerAvatarUrl: String?,
    val languages: List<Language>
) {
    /**
     * Represents a programming language used in the repository.
     */
    data class Language(
        val name: String,
        val color: String?,
        val size: Int
    )
}
