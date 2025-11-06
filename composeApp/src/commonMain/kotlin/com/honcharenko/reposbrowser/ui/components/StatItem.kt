package com.honcharenko.reposbrowser.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Stat item component for displaying statistics (stars, forks, etc.).
 *
 * @param icon Icon to display
 * @param count Number to display
 * @param contentDescription Content description for accessibility
 * @param modifier Modifier for the container
 */
@Composable
fun StatItem(
    icon: ImageVector,
    count: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = formatCount(count),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Formats count with K suffix for thousands
 */
private fun formatCount(count: Int): String {
    return when {
        count >= 1000 -> "${count / 1000}k"
        else -> count.toString()
    }
}
