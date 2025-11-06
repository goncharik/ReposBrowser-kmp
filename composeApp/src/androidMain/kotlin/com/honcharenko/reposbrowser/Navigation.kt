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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.honcharenko.reposbrowser.ui.screens.SearchScreen
import com.honcharenko.reposbrowser.ui.theme.AppTheme

/**
 * Sealed class representing app navigation routes
 */
sealed class Screen(val route: String, val title: String) {
    data object Search : Screen("search", "Search")
    data object Favorites : Screen("favorites", "Favorites")
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

        val screens = listOf(Screen.Search, Screen.Favorites)

        // Get current screen title based on route
        val currentScreen = screens.find { screen ->
            currentDestination?.hierarchy?.any { it.route == screen.route } == true
        } ?: Screen.Search

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
                screens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.Search -> Icons.Default.Search
                                    Screen.Favorites -> Icons.Default.Star
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
                        // TODO: Navigate to repository details screen when implemented
                        println("Repository clicked: ${repository.nameWithOwner}")
                    }
                )
            }

            composable(Screen.Favorites.route) {
                // TODO: Replace with FavoritesScreen when implemented
                FavoritesPlaceholder()
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
