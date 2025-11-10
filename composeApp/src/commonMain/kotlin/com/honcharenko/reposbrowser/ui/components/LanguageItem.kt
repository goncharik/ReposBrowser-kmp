package com.honcharenko.reposbrowser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Displays a programming language with a colored dot indicator, name, and percentage.
 *
 * @param name Language name (e.g., "Kotlin", "Swift")
 * @param color Hex color string from GitHub API (e.g., "#A97BFF", can be null)
 * @param percentage Percentage of codebase (e.g., 85.3)
 * @param modifier Optional modifier for the component
 */
@Composable
fun LanguageItem(
    name: String,
    color: String?,
    percentage: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Colored dot indicator
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(parseHexColor(color))
        )

        // Language name
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        // Percentage
        Text(
            text = "${(percentage * 10).toInt() / 10.0}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Parses a hex color string to a Compose Color.
 * Returns a default gray color if parsing fails or color is null.
 *
 * @param hexColor Hex color string with optional # prefix (e.g., "#A97BFF" or "A97BFF")
 * @return Parsed Color or default gray
 */
private fun parseHexColor(hexColor: String?): Color {
    if (hexColor == null) return Color(0xFF9E9E9E) // Default gray

    return try {
        val cleanHex = hexColor.removePrefix("#")
        val rgb = cleanHex.toLong(16)

        Color(
            red = ((rgb shr 16) and 0xFF).toInt(),
            green = ((rgb shr 8) and 0xFF).toInt(),
            blue = (rgb and 0xFF).toInt(),
            alpha = 255
        )
    } catch (e: Exception) {
        Color(0xFF9E9E9E) // Default gray on parse error
    }
}
