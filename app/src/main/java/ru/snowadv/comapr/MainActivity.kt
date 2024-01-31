package ru.snowadv.comapr

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.presentation.ui.login.LoginScreen
import ru.snowadv.comapr.presentation.ui.home.HomeScreen
import ru.snowadv.comapr.presentation.ui.roadmap.single.RoadMapScreen
import ru.snowadv.comapr.presentation.view_model.MainViewModel
import ru.snowadv.comapr.presentation.view_model.RoadMapViewModel
import ru.snowadv.comapr.ui.theme.ComaprTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val mainViewModel: MainViewModel = hiltViewModel()
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()

            val uriHandler = LocalUriHandler.current

            LaunchedEffect(true) {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainViewModel.eventFlow.onEach { // subscribe to ui events
                        Log.d(TAG, "got event: $it")
                        when (it) {
                            is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(it.message)
                            is UiEvent.OpenLink -> uriHandler.openUri(it.url)
                        }
                    }.launchIn(this)

                    mainViewModel.navigationFlow.onEach { navEvent ->  // subscribe to navigation events
                        when (navEvent) {
                            is NavigationEvent.ToHomeScreen, is NavigationEvent.ToLoginScreen -> {
                                navController.navigate(navEvent.route) {
                                    popUpTo(navEvent.route) {
                                        inclusive = true
                                        saveState = true
                                    }
                                }
                                navController.graph.setStartDestination(navEvent.route)
                                Log.d(TAG, "navigating to ${navEvent.route}, backstack: ${navController.backQueue.map { it.destination }}")
                            }
                            else -> {
                                navController.navigate(navEvent.route)
                            }
                        }
                    }.launchIn(this)
                }
            }

            ComaprTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier.fillMaxSize(),
                ) { paddingValues ->

                    Column(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        if(mainViewModel.loading.value) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        NavHost(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            startDestination = "home"
                        ) {
                            composable("login") {
                                LoginScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    mainViewModel = mainViewModel
                                )
                            }

                            composable("home") {
                                HomeScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    mainViewModel = mainViewModel
                                )
                            }

                            composable(
                                "roadmap/{roadmapId}",
                                arguments = listOf(navArgument("roadmapId") {
                                    type = NavType.LongType
                                })
                            ) { navBackStackEntry ->
                                val roadmapId = navBackStackEntry.arguments?.getLong("roadmapId")
                                // by some reason getLong returns 0 here but string casted to long works correctly. Why?
                                // TODO: fix
                                val roadMapViewModel: RoadMapViewModel = hiltViewModel()

                                roadmapId?.let {
                                    RoadMapScreen(
                                        modifier = Modifier.fillMaxSize(),
                                        roadMapViewModel = roadMapViewModel,
                                        mainViewModel = mainViewModel,
                                        roadMapId = it
                                    )
                                } ?: error("Id wasn't passed as roadmap nav argument!")
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComaprTheme {
        Greeting("Android")
    }
}