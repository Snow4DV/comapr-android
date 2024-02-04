package ru.snowadv.comapr.presentation.ui.session.single

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.SampleData
import ru.snowadv.comapr.core.util.UiEvent
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
        }
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

