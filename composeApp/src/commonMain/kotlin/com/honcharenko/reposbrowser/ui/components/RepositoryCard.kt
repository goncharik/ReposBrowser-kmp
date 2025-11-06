package com.honcharenko.reposbrowser.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.honcharenko.reposbrowser.data.model.Repository
import com.honcharenko.reposbrowser.ui.theme.StarYellow

/**
 * Repository card component.
 * Displays repository information including owner avatar, name, description, and stats.
 *
 * @param repository Repository data to display
 * @param isFavorite Whether the repository is favorited
 * @param onFavoriteClick Callback when favorite button is clicked
 * @param onClick Callback when card is clicked
 * @param modifier Modifier for the card
 */
@Composable
fun RepositoryCard(
    repository: Repository,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header row with avatar and owner name
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Owner avatar
                AsyncImage(
                    model = repository.ownerAvatarUrl,
                    contentDescription = "${repository.ownerLogin}'s avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    onLoading = {
                        println("Loading image: ${repository.ownerAvatarUrl}")
                    },
                    onSuccess = {
                        println("Successfully loaded image: ${repository.ownerAvatarUrl}")
                    },
                    onError = { state ->
                        println("Error loading image: ${repository.ownerAvatarUrl}")
                        println("Error: ${state.result.throwable.message}")
                    }
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Repository name
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = repository.nameWithOwner,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Favorite button
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Description
            if (!repository.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = repository.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Stats row
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stars
                StatItem(
                    icon = Icons.Default.Star,
                    count = repository.stargazersCount,
                    contentDescription = "Stars"
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Language badge
                if (!repository.language.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Language color indicator (if available)
                        // For now, just show the language name
                        Text(
                            text = repository.language,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
