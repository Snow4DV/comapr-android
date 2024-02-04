package ru.snowadv.comapr.presentation.ui.session.single

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.SampleData
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.presentation.ui.common.TextWithIcon
import ru.snowadv.comapr.presentation.ui.roadmap.common.RoadMapAndOrSessionScreenContent
import ru.snowadv.comapr.presentation.ui.session.common.SessionItem
import ru.snowadv.comapr.presentation.view_model.MainViewModel
import ru.snowadv.comapr.presentation.view_model.SessionViewModel


@Composable
fun SessionScreen(
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel = hiltViewModel(),
    mainViewModel: MainViewModel
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    val state = sessionViewModel.state.value

    LaunchedEffect(true) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sessionViewModel.getSession()
        }
    }


    RoadMapAndOrSessionScreenContent(
        modifier = modifier,
        isSession = true,
        taskStates = state.session?.finishedTasksIds?.toSet() ?: emptySet(),
        onBackClicked = { mainViewModel.navigate(NavigationEvent.PopBackStack) },
        roadMap = state.session?.roadMap,
        loading = state.loading,
        onRefresh = { sessionViewModel.getSession() },
        onUrlClick = {
            mainViewModel.sendUiEvent(UiEvent.OpenLink(it))
        },
        onTaskChecked = { task, taskState ->
            sessionViewModel.markTask(task.id, taskState)
        },
        sessionComposable = state.session?.let {
            {
                SessionItem(
                    modifier = Modifier.fillMaxWidth(),
                    session = it,
                    showChatUrl = true,
                    onUrlClick = {
                        mainViewModel.sendUiEvent(UiEvent.OpenLink(it))
                    },
                    onJoinSessionClick = if (!it.joined) {
                        {
                            sessionViewModel.joinSession()
                        }
                    } else {
                        null
                    },
                    onLeaveSessionClick = if (it.joined) {
                        {
                            sessionViewModel.leaveSession()
                        }
                    } else {
                        null
                    },
                    onStartSessionClick = {
                        sessionViewModel.startSession()
                    },
                    onEndSessionClick = {
                        sessionViewModel.endSession()
                    }
                )
            }
        },
        sessionStatsComposable = state.session?.let {
            {
                SessionStatsItem(
                    modifier = Modifier.fillMaxWidth(),
                    session = state.session
                )
            }
        },
        participantsCount = state.session?.users?.size ?: 0
    )

}


@Composable
fun SessionStatsItem(
    modifier: Modifier = Modifier,
    session: MapSession,
) {
    Card(
        modifier = modifier
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {

            session.users.forEach { userCompState ->
                val percents =
                    if (session.roadMap.tasksCount != 0) {
                        (userCompState.finishedTasksIds.size.toDouble() / session.roadMap.tasksCount * 100).toInt()
                    } else {
                        0
                    }
                TextWithIcon(
                    text = "${userCompState.user.username}: ${userCompState.finishedTasksIds.size}/${session.roadMap.tasksCount} (${percents}%)",
                    fontSize = 18.sp,
                    icon = Icons.Outlined.Person
                )
            }

        }
    }


}

@Preview
@Composable
fun SessionItemPreview() {
    SessionStatsItem(
        modifier = Modifier
            .width(420.dp),
        session = SampleData.session
    )
}


@Preview
@Composable
fun SessionScreenPreview() {
    RoadMapAndOrSessionScreenContent(
        modifier = Modifier
            .width(420.dp)
            .height(905.dp),
        roadMap = SampleData.roadMap,
        onBackClicked = {},
        loading = true,
        onRefresh = {},
        onUrlClick = {},
        onCreateSession = {}
    )
}

