package ru.snowadv.comapr.presentation.ui.session.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.SampleData
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.presentation.ui.common.TextWithIcon
import ru.snowadv.comapr.presentation.ui.session.common.SessionItem
import ru.snowadv.comapr.presentation.view_model.MainViewModel
import ru.snowadv.comapr.presentation.view_model.SessionsViewModel


@Composable
fun SessionsScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    sessionsViewModel: SessionsViewModel,
    showOnlyUserActiveSessions: Boolean = false
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(showOnlyUserActiveSessions) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sessionsViewModel.loadDataIfDidntLoadBefore(showOnlyUserActiveSessions)
        }
    }

    SessionsScreenContent(
        modifier = modifier,
        state = sessionsViewModel.state.value,
        onRefresh = { if (showOnlyUserActiveSessions) sessionsViewModel.getActiveSessionsForCurrentUser() else sessionsViewModel.getSessions() },
        onClickSession = { mainViewModel.navigate(NavigationEvent.ToSession(it.id)) },
        onCreateSession = { mainViewModel.navigate(NavigationEvent.ToSessionEditor()) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SessionsScreenContent(
    modifier: Modifier = Modifier,
    state: SessionsScreenState,
    onRefresh: () -> Unit,
    onClickSession: (MapSession) -> Unit,
    onCreateSession: () -> Unit
) {

    val pullRefreshState =
        rememberPullRefreshState(state.loading, onRefresh)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateSession,
            ) {
                Icon(Icons.Filled.Add, "Create session")
            }

        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState),

            ) {
            SessionsList(
                modifier = Modifier.fillMaxSize(),
                sessions = state.sessions,
                onClickSession = onClickSession
            )

            PullRefreshIndicator(
                refreshing = state.loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SessionsList(
    modifier: Modifier = Modifier,
    sessions: List<MapSession>,
    onClickSession: (MapSession) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(sessions) { session ->
            SessionItem(
                session = session,
                onClickSession = onClickSession
            )

        }
    }
}


@Preview
@Composable
fun TextWithIconPreview() {
    TextWithIcon(
        icon = Icons.Filled.Person,
        text = "Test user",
        fontSize = 23.sp,
        color = MaterialTheme.colorScheme.primary
    )
}



