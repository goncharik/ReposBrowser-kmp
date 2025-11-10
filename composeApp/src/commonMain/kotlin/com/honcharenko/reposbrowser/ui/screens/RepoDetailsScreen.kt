package com.honcharenko.reposbrowser.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.honcharenko.reposbrowser.data.model.Repository
import com.honcharenko.reposbrowser.data.model.RepositoryDetails
import com.honcharenko.reposbrowser.ui.components.LanguageItem
import com.honcharenko.reposbrowser.ui.components.LoadingIndicator
import com.honcharenko.reposbrowser.ui.components.StatItem
import com.honcharenko.reposbrowser.util.DateFormatter
import com.honcharenko.reposbrowser.viewmodel.RepoDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Repository details screen showing comprehensive information about a GitHub repository.
 *
 * Features:
 * - Displays basic repository info immediately from the Repository object
 * - Loads full details from API in the background
 * - Owner avatar and repository name
 * - Favorite toggle button
 * - Repository description
 * - Statistics (stars, forks, watchers, issues, PRs)
 * - Metadata (created date, updated date, license)
 * - Language breakdown with percentages
 * - Links to homepage and GitHub repository
 *
 * @param repository Basic repository object with immediate display data
 * @param onNavigateBack Callback for back navigation
 * @param viewModel Injected ViewModel (defaults to Koin)
 */
@Composable
fun RepoDetailsScreen(
        repository: Repository,
        onNavigateBack: () -> Unit = {},
        viewModel: RepoDetailsViewModel = koinViewModel()
) {
    val repositoryDetails by viewModel.repositoryDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Load full repository details on initial composition
    LaunchedEffect(repository.ownerLogin, repository.name) {
        viewModel.loadDetails(repository.ownerLogin, repository.name)
    }

    // Show error in snackbar
    LaunchedEffect(error) {
        error?.let { errorMessage ->
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Always show content - use basic repository data initially, then full details when loaded
        DetailsContent(
                repository = repository,
                details = repositoryDetails,
                isFavorite = isFavorite,
                isLoadingDetails = isLoading,
                onFavoriteClick = { viewModel.toggleFavorite() },
                onRetry = { viewModel.loadDetails(repository.ownerLogin, repository.name) },
                modifier = Modifier.fillMaxSize()
        )

        // Snackbar host at bottom
        SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        )
    }
}

/**
 * Details content with scrollable sections. Shows basic repository info immediately, and
 * progressively enhances with full details.
 */
@Composable
private fun DetailsContent(
        repository: Repository,
        details: RepositoryDetails?,
        isFavorite: Boolean,
        isLoadingDetails: Boolean,
        onFavoriteClick: () -> Unit,
        onRetry: () -> Unit,
        modifier: Modifier = Modifier
) {
    Column(
            modifier = modifier.verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header: Avatar, Name, Favorite button (always visible with basic data)
        HeaderSection(
                ownerAvatarUrl = repository.ownerAvatarUrl,
                nameWithOwner = repository.nameWithOwner,
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick
        )

        // Description (from basic data)
        if (!repository.description.isNullOrBlank()) {
            Text(
                    text = repository.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Basic Statistics (always available from repository object)
        BasicStatsSection(repository = repository)

        // Show loading indicator or full details
        when {
            isLoadingDetails && details == null -> {
                // Show loading indicator while fetching full details
                Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                ) { LoadingIndicator(message = "Loading additional details...") }
            }
            details != null -> {
                // Full details loaded - show additional information
                HorizontalDivider()

                // Extended Statistics (watchers, issues, PRs)
                ExtendedStatsSection(details = details)

                HorizontalDivider()

                // Metadata (Created, Updated, License)
                MetadataSection(
                        createdAt = details.createdAt,
                        updatedAt = details.updatedAt,
                        license = details.license
                )

                // Languages breakdown
                if (details.languages.isNotEmpty()) {
                    HorizontalDivider()
                    LanguagesSection(details = details)
                }

                // Links
                if (!details.homepageUrl.isNullOrBlank() || details.url.isNotBlank()) {
                    HorizontalDivider()
                    LinksSection(homepageUrl = details.homepageUrl, repoUrl = details.url)
                }

                // Spacer at the end for better UX
                Spacer(modifier = Modifier.height(58.dp))
            }
        }
    }
}

/** Header with avatar, repo name, and favorite button */
@Composable
private fun HeaderSection(
        ownerAvatarUrl: String?,
        nameWithOwner: String,
        isFavorite: Boolean,
        onFavoriteClick: () -> Unit
) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        // Owner avatar
        AsyncImage(
                model = ownerAvatarUrl,
                contentDescription = "Owner avatar",
                modifier =
                        Modifier.size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        // Repository name
        Text(
                text = nameWithOwner,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
        )

        // Favorite button
        IconButton(onClick = onFavoriteClick) {
            Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription =
                            if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint =
                            if (isFavorite) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Basic statistics from Repository object (stars, forks) */
@Composable
private fun BasicStatsSection(repository: Repository) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
        )

        // Stars and Forks (always available)
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatItem(
                    icon = Icons.Filled.Star,
                    count = repository.stargazersCount,
                    contentDescription = "Stars"
            )
            StatItem(
                    icon = Icons.Filled.Star, // TODO: Use fork icon when available
                    count = repository.forksCount,
                    contentDescription = "Forks"
            )

            // Show primary language if available
            if (!repository.language.isNullOrBlank()) {
                Text(
                        text = repository.language,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

/** Extended statistics from RepositoryDetails (watchers, issues, PRs) */
@Composable
private fun ExtendedStatsSection(details: RepositoryDetails) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
                text = "Additional Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
        )

        // Watchers
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatItem(
                    icon = Icons.Filled.Star, // TODO: Use watcher icon when available
                    count = details.watchersCount,
                    contentDescription = "Watchers"
            )
        }

        // Issues and Pull Requests
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                    text = "${details.openIssuesCount} open issues",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                    text = "${details.openPullRequestsCount} open PRs",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Metadata section (created, updated, license) */
@Composable
private fun MetadataSection(createdAt: String, updatedAt: String, license: String?) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
                text = "Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
        )

        // Created date
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                    text = "Created:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                    text = DateFormatter.formatCreatedDate(createdAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Updated date (relative)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                    text = "Updated:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                    text = DateFormatter.formatRelativeTime(updatedAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )
        }

        // License
        if (!license.isNullOrBlank()) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                        text = "License:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                        text = license,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/** Languages breakdown section */
@Composable
private fun LanguagesSection(details: RepositoryDetails) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
                text = "Languages",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
        )

        // Calculate total size for percentages
        val totalSize = details.languages.sumOf { it.size }

        // Display each language with percentage
        details.languages.forEach { language ->
            val percentage = (language.size.toFloat() / totalSize.toFloat()) * 100f
            LanguageItem(name = language.name, color = language.color, percentage = percentage)
        }
    }
}

/** Links section (homepage, GitHub URL) */
@Composable
private fun LinksSection(homepageUrl: String?, repoUrl: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
                text = "Links",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
        )

        // Homepage URL
        if (!homepageUrl.isNullOrBlank()) {
            Text(
                    text = "Homepage: $homepageUrl",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
            )
        }

        // GitHub URL
        Text(
                text = "GitHub: $repoUrl",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
        )
    }
}
