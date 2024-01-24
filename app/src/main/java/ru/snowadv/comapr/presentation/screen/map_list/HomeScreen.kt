package ru.snowadv.comapr.presentation.screen.map_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.snowadv.comapr.R
import ru.snowadv.comapr.presentation.view_model.MainViewModel



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

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
) {
    val navController = rememberNavController()
    val selectedItemIndex = rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                BottomNavigationItem.entries.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex.intValue == index,
                        onClick = {
                            selectedItemIndex.intValue = index
                            navController.navigate(item.navRoute)
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        alwaysShowLabel = false,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex.intValue) {
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

            }
            composable("sessions") {

            }
            composable("profile") {

            }
        }
    }

}