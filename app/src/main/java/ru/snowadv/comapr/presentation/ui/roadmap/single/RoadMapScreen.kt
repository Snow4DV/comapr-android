package ru.snowadv.comapr.presentation.ui.roadmap.single

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.SampleData
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.presentation.ui.common.RoadMapAndOrSessionScreenContent
import ru.snowadv.comapr.presentation.view_model.MainViewModel
import ru.snowadv.comapr.presentation.view_model.RoadMapViewModel


@Composable
fun RoadMapScreen(
    modifier: Modifier = Modifier,
    roadMapViewModel: RoadMapViewModel,
    mainViewModel: MainViewModel,
    roadMapId: Long
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(true) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            roadMapViewModel.getRoadMap(roadMapId)
        }
    }


    RoadMapAndOrSessionScreenContent(
        modifier = modifier,
        onBackClicked = { mainViewModel.navigate(NavigationEvent.BackToHomeScreen) },
        roadMap = roadMapViewModel.state.value.roadMap,
        loading = roadMapViewModel.state.value.loading,
        onRefresh = { roadMapViewModel.getRoadMap(roadMapId) },
        onUrlClick = {
            mainViewModel.sendUiEvent(UiEvent.OpenLink(it))
        }
    )

}






@Preview
@Composable
fun RoadMapItemPreview() {
    RoadMapAndOrSessionScreenContent(
        modifier = Modifier
            .width(420.dp)
            .height(905.dp),
        roadMap = SampleData.roadMap,
        onBackClicked = {},
        loading = true,
        onRefresh = {},
        onUrlClick = {}
    )
}

