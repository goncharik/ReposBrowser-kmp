package com.honcharenko.reposbrowser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.honcharenko.reposbrowser.data.model.Repository
import com.honcharenko.reposbrowser.ui.screens.FavoritesScreen
import com.honcharenko.reposbrowser.ui.screens.RepoDetailsScreen
import com.honcharenko.reposbrowser.ui.screens.SearchScreen
import com.honcharenko.reposbrowser.ui.theme.AppTheme
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Sealed class representing app navigation routes
 */
sealed class Screen(val route: String, val title: String) {
    data object Search : Screen("search", "Search")
    data object Favorites : Screen("favorites", "Favorites")
    data object Details : Screen("details/{repoData}", "Repository Details") {
        fun createRoute(repository: Repository): String {
            val encodedData = encodeRepositoryData(repository)
            return "details/$encodedData"
        }
    }
}

/**
 * Encodes repository data for navigation
 */
private fun encodeRepositoryData(repository: Repository): String {
    // Simple encoding: join fields with | separator and URL-encode
    val data = listOf(
        repository.id,
        repository.name,
        repository.nameWithOwner,
        repository.description ?: "",
        repository.stargazersCount.toString(),
        repository.forksCount.toString(),
        repository.language ?: "",
        repository.languageColor ?: "",
        repository.ownerLogin,
        repository.ownerAvatarUrl ?: "",
        repository.url
    ).joinToString("|")
    return URLEncoder.encode(data, StandardCharsets.UTF_8.toString())
}

/**
 * Decodes repository data from navigation
 */
private fun decodeRepositoryData(encodedData: String): Repository {
    val data = URLDecoder.decode(encodedData, StandardCharsets.UTF_8.toString()).split("|")
    return Repository(
        id = data[0],
        name = data[1],
        nameWithOwner = data[2],
        description = data[3].ifBlank { null },
        stargazersCount = data[4].toInt(),
        forksCount = data[5].toInt(),
        language = data[6].ifBlank { null },
        languageColor = data[7].ifBlank { null },
        ownerLogin = data[8],
        ownerAvatarUrl = data[9].ifBlank { null },
        url = data[10]
    )
}

/**
 * Main navigation composable for Android app.
 * Uses bottom navigation bar with NavHost for screen switching.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    AppTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        // Only bottom navigation screens (not details or other full-screen destinations)
        val bottomNavScreens = listOf(Screen.Search, Screen.Favorites)

        // Get current screen title based on route
        val currentScreen = when (currentDestination?.route) {
            Screen.Search.route -> Screen.Search
            Screen.Favorites.route -> Screen.Favorites
            else -> if (currentDestination?.route?.startsWith("details/") == true) {
                Screen.Details
            } else {
                Screen.Search
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentScreen.title) },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
        bottomBar = {
            NavigationBar {
                bottomNavScreens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.Search -> Icons.Default.Search
                                    Screen.Favorites -> Icons.Default.Star
                                    else -> Icons.Default.Search // Fallback (shouldn't happen with bottomNavScreens)
                                },
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination to avoid building up a large stack
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Search.route) {
                SearchScreen(
                    onRepositoryClick = { repository ->
                        navController.navigate(Screen.Details.createRoute(repository))
                    }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onRepositoryClick = { repository ->
                        navController.navigate(Screen.Details.createRoute(repository))
                    }
                )
            }

            composable(
                route = Screen.Details.route,
                arguments = listOf(
                    navArgument("repoData") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val encodedData = backStackEntry.arguments?.getString("repoData") ?: ""
                val repository = decodeRepositoryData(encodedData)

                RepoDetailsScreen(
                    repository = repository,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
        }
    }
}

/**
 * Placeholder for Favorites screen (to be implemented in Phase 3)
 */
@Composable
private fun FavoritesPlaceholder() {
    Box(
        modifier = Modifier.padding(PaddingValues(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Favorites Screen - Coming Soon",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
