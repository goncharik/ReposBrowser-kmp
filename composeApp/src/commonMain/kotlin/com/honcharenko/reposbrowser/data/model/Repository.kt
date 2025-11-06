package com.honcharenko.reposbrowser.data.model

/**
 * Domain model for a GitHub repository (simplified view for lists).
 * This is separate from the Apollo-generated GraphQL types to decouple
 * the domain layer from the API layer.
 */
data class Repository(
    val id: String,
    val name: String,
    val nameWithOwner: String,
    val description: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val language: String?,
    val languageColor: String?,
    val ownerLogin: String,
    val ownerAvatarUrl: String?,
    val url: String
)
