package ru.snowadv.comapr.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.snowadv.comapr.R
import ru.snowadv.comapr.presentation.ui.profile.ProfileScreen
import ru.snowadv.comapr.presentation.ui.roadmap.list.RoadMapsScreen
import ru.snowadv.comapr.presentation.ui.session.list.SessionsScreen
import ru.snowadv.comapr.presentation.view_model.MainViewModel


enum class BottomNavigationItem(
    val navRoute: String,
    val bottomTitleResId: Int,
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    RoadMaps(
        "roadmaps",
        R.string.road_maps,
        R.string.road_maps,
        Icons.Filled.Place,
        Icons.Outlined.Place
    ),
    PublicSessions(
        "sessions", R.string.public_sessions_bottom,
        R.string.public_sessions, Icons.Filled.List, Icons.Outlined.List
    ),
    MyActiveSessions(
        "activeSessions",
        R.string.active_sessions_bottom,
        R.string.active_sessions,
        Icons.Filled.CheckCircle,
        Icons.Outlined.CheckCircle
    ),
    Profile(
        "profile",
        R.string.profile,
        R.string.profile,
        Icons.Filled.Person,
        Icons.Outlined.Person
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
) {
    val navController = rememberNavController()
    val selectedScreenIndex = rememberSaveable { mutableIntStateOf(0) }


    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = BottomNavigationItem.entries[selectedScreenIndex.intValue].titleResId)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        },
        bottomBar = {
            NavigationBar {
                BottomNavigationItem.entries.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedScreenIndex.intValue == index,
                        onClick = {
                            selectedScreenIndex.intValue = index
                            navController.navigate(item.navRoute) {
                                popUpTo(navController.graph.startDestinationRoute!!) {
                                    saveState = true

                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            Log.d(
                                "HomeScreen",
                                "navigating to ${item.navRoute}, backstack: ${navController.backQueue.map { it.destination }}"
                            )
                            println()
                        },
                        label = {
                            Text(text = stringResource(id = item.bottomTitleResId))
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedScreenIndex.intValue) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = stringResource(id = item.bottomTitleResId)
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            navController = navController,
            startDestination = "roadmaps"
        ) {
            composable("roadmaps") {
                RoadMapsScreen(
                    mainViewModel = mainViewModel,
                    modifier = Modifier.fillMaxSize(),
                    roadMapsViewModel = hiltViewModel()
                )
            }
            composable(
                route = "sessions"
            ) {
                SessionsScreen(
                    modifier = Modifier.fillMaxSize(),
                    mainViewModel = mainViewModel,
                    sessionsViewModel = hiltViewModel(),
                    showOnlyUserActiveSessions = false
                )
            }
            composable(
                route = "activeSessions"
            ) {
                SessionsScreen(
                    modifier = Modifier.fillMaxSize(),
                    mainViewModel = mainViewModel,
                    sessionsViewModel = hiltViewModel(),
                    showOnlyUserActiveSessions = true
                )
            }
            composable("profile") {
                ProfileScreen(
                    modifier = Modifier.fillMaxSize(),
                    profileViewModel = hiltViewModel(),
                    mainViewModel = mainViewModel
                )
            }
        }
    }

}