package ru.snowadv.comapr.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
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
import ru.snowadv.comapr.presentation.view_model.ProfileViewModel
import ru.snowadv.comapr.presentation.view_model.RoadMapsViewModel
import ru.snowadv.comapr.presentation.view_model.SessionsViewModel


enum class BottomNavigationItem(
    val navRoute: String,
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    RoadMaps("roadmaps", R.string.road_maps, Icons.Filled.Place, Icons.Outlined.Place),
    Sessions("sessions", R.string.sessions, Icons.Filled.List, Icons.Outlined.List),
    Profile("profile", R.string.profile, Icons.Filled.Person, Icons.Outlined.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
) {
    val navController = rememberNavController()
    val selectedScreenIndex = rememberSaveable { mutableIntStateOf(0) }

    val profileViewModel: ProfileViewModel = hiltViewModel()
    val roadMapsViewModel: RoadMapsViewModel = hiltViewModel()
    val sessionViewModel: SessionsViewModel = hiltViewModel()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(BottomNavigationItem.entries[selectedScreenIndex.intValue].name) },
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
                            Text(text = stringResource(id = item.titleResId))
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedScreenIndex.intValue) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = stringResource(id = item.titleResId)
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
                    roadMapsViewModel = roadMapsViewModel
                )
            }
            composable("sessions") {
                SessionsScreen(
                    modifier = Modifier.fillMaxSize(),
                    mainViewModel = mainViewModel,
                    sessionsViewModel = sessionViewModel
                )
            }
            composable("profile") {
                ProfileScreen(
                    modifier = Modifier.fillMaxSize(),
                    profileViewModel = profileViewModel,
                    mainViewModel = mainViewModel
                )
            }
        }
    }

}