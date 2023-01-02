package com.gmillz.gallery.ui.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoAlbum
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gmillz.gallery.R
import com.gmillz.gallery.ui.gallery.GalleryScreen

private val navItems = listOf(Screen.Timeline, Screen.Albums, Screen.Videos)

@ExperimentalMaterial3Api
@Composable
fun MainContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(
        bottomBar = {
            NavigationBar(
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                navItems.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(screen.icon, contentDescription = screen.route)
                        },
                        label = {
                            Text(
                                text = stringResource(id = screen.titleResourceId),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        selected = navBackStackEntry?.destination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
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
            startDestination = Screen.Timeline.route,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable(Screen.Timeline.route) {
                GalleryScreen()
            }
            composable(Screen.Albums.route) {
                Text(text = "Albums")
            }
            composable(Screen.Videos.route) {
                Text(text = "Videos")
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val titleResourceId: Int, val icon: ImageVector) {
    object Timeline: Screen("timeline", R.string.title_timeline, Icons.Outlined.Timeline)
    object Albums: Screen("albums", R.string.title_albums, Icons.Outlined.PhotoAlbum)
    object Videos: Screen("videos", R.string.title_videos, Icons.Outlined.VideoLibrary)
}