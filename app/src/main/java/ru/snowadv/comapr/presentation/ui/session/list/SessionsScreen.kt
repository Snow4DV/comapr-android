package ru.snowadv.comapr.presentation.ui.session.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import ru.snowadv.comapr.R
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.SampleData
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.presentation.ui.common.TextWithIcon
import ru.snowadv.comapr.presentation.view_model.MainViewModel
import ru.snowadv.comapr.presentation.view_model.SessionsViewModel
import java.time.format.DateTimeFormatter


@Composable
fun SessionsScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    sessionsViewModel: SessionsViewModel
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(true) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sessionsViewModel.loadDataIfDidntLoadBefore()
        }
    }

    SessionsScreenContent(
        modifier = modifier,
        state = sessionsViewModel.state.value,
        onRefresh = {sessionsViewModel.getSessions()},
        onClickSession = {mainViewModel.navigate(NavigationEvent.ToSession(it.id))},
        onCreateSession = {mainViewModel.navigate(NavigationEvent.ToSessionEditor())}
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
@Composable
fun SessionItem(
    modifier: Modifier = Modifier,
    session: MapSession,
    onClickSession: (MapSession) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .padding(5.dp)
            .clickable { onClickSession(session) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .height(with(LocalDensity.current) { 23.sp.toDp() })
                        .padding(end = 5.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = session.creator.username,
                    fontSize = 23.sp,
                    color = MaterialTheme.colorScheme.primary
                )

            }
            TextWithIcon(
                text = "${stringResource(R.string.map_sessions_list)}${session.roadMap.name}",
                fontSize = 18.sp,
                icon = Icons.Outlined.Place
            )
            TextWithIcon(
                text = "${stringResource(R.string.joined_users)}${session.users.size}",
                fontSize = 18.sp,
                icon = ImageVector.vectorResource(R.drawable.people_outlined)
            )
            TextWithIcon(
                text = "${stringResource(R.string.starting_at)}${
                    session.startDate.format(
                        DateTimeFormatter.ofPattern("yyyy.MM.DD hh:mm")
                    )
                }",
                fontSize = 18.sp,
                icon = ImageVector.vectorResource(R.drawable.clock_outlined)
            )
            session.groupChatUrl?.let { TextWithIcon(
                text = stringResource(R.string.with_group_chat),
                fontSize = 18.sp,
                icon = ImageVector.vectorResource(R.drawable.chat_outlined)
            ) }
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


@Preview
@Composable
fun SessionItemPreview() {
    SessionItem(
        modifier = Modifier
            .width(420.dp),
        session = SampleData.session,
        onClickSession = {}
    )
}

