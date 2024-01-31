package ru.snowadv.comapr.presentation.ui.roadmap.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import ru.snowadv.comapr.R
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.SampleData
import ru.snowadv.comapr.domain.model.Category
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.presentation.ui.common.GroupHeader
import ru.snowadv.comapr.presentation.view_model.MainViewModel
import ru.snowadv.comapr.presentation.view_model.RoadMapsViewModel
import ru.snowadv.comaprbackend.dto.CategorizedRoadMaps

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RoadMapsScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    roadMapsViewModel: RoadMapsViewModel
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(true) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            roadMapsViewModel.loadDataIfDidntLoadBefore()
        }
    }

    val filterDialogVisibility = remember {mutableStateOf(false)}

    val state = roadMapsViewModel.state
    val pullRefreshState =
        rememberPullRefreshState(state.value.loading, { roadMapsViewModel.getRoadMaps() })
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    filterDialogVisibility.value = true
                },
            ) {
                Icon(painterResource(id = R.drawable.filter_filled), "Filter action button")
            }

        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState),

            ) {
            RoadMapsCategorizedList(
                modifier = Modifier.fillMaxSize(),
                categorizedRoadMaps = state.value.roadMaps,
                onClickRoadMap = {
                    mainViewModel.navigate(NavigationEvent.ToRoadMap(it.id))
                }
            )

            PullRefreshIndicator(
                refreshing = state.value.loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }


    if(filterDialogVisibility.value) {
        FilterCategoryAndStatusDialog(
            categories = state.value.getCategories(),
            statuses = RoadMapsScreenState.getVerificationStatuses(),
            onSubmitButtonClick = {statusId, categoryId ->
                roadMapsViewModel.setStatusIdAndCategoryIdFilters(statusId, categoryId)
            },
            onDismiss = {filterDialogVisibility.value = false},
            currentCategoryId = state.value.filterCategoryId,
            currentStatusId = state.value.filterStatusId
        )
    }


}



@Composable
fun FilterCategoryAndStatusDialog(
    categories: List<Category>,
    currentCategoryId: Long?,
    statuses: List<RoadMap.VerificationStatus>,
    currentStatusId: Int?,
    onSubmitButtonClick: (statusId: Int?, categoryId: Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedCategory = remember { mutableIntStateOf(categories.indexOfFirst { it.id == currentCategoryId }) }
    val selectedStatus = remember { mutableIntStateOf(statuses.indexOfFirst { it.id == currentStatusId }) }

    Dialog(onDismissRequest = {onDismiss()}) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(10.dp)
        ) {

            Column(modifier = Modifier.padding(10.dp)) {

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.filters),
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth())

                Text(text = stringResource(R.string.category_filter_title))
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedCategory.intValue == -1,
                                onClick = { selectedCategory.intValue = -1 }
                            )
                            Text(stringResource(R.string.none_filter))
                        }
                    }
                    itemsIndexed(categories) { index, it ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedCategory.intValue == index,
                                onClick = { selectedCategory.intValue = index }
                            )
                            Text(it.name)
                        }
                    }
                }

                Spacer(modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth())

                Text(text = stringResource(R.string.status_filter_title))
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()) {

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedStatus.intValue == -1,
                                onClick = { selectedStatus.intValue = -1 }
                            )
                            Text(stringResource(R.string.none_filter))
                        }
                    }
                    itemsIndexed(statuses) { index, it ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedStatus.intValue == index,
                                onClick = { selectedStatus.intValue = index }
                            )
                            Text(stringResource(id = it.textResId))
                        }
                    }
                }

                Spacer(modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth())

                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        onSubmitButtonClick.invoke(
                            selectedStatus.intValue.let { if(it == -1) null else statuses[it].id },
                            selectedCategory.intValue.let { if(it == -1) null else categories[it].id }
                        )
                        onDismiss()
                    },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = stringResource(R.string.filter))
                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoadMapsCategorizedList(
    modifier: Modifier = Modifier,
    categorizedRoadMaps: List<CategorizedRoadMaps>,
    onClickRoadMap: (RoadMap) -> Unit
) {

    val categoryToHidden = remember { mutableStateMapOf<Long, Boolean>() }
    LazyColumn(
        modifier = modifier
    ) {
        categorizedRoadMaps.forEach { categorizedMaps ->
            stickyHeader {
                GroupHeader(
                    modifier = Modifier.clickable {
                        categoryToHidden[categorizedMaps.categoryId] =
                            !(categoryToHidden[categorizedMaps.categoryId] ?: true)
                    },
                    title = categorizedMaps.categoryName
                )
            }
            items(categorizedMaps.roadMaps) { roadMap ->
                AnimatedVisibility(visible = categoryToHidden[categorizedMaps.categoryId] ?: true) {
                    RoadMapItem(
                        roadMap = roadMap,
                        onClickRoadMap = onClickRoadMap
                    )
                }

            }

        }
    }
}



@Composable
fun RoadMapItem(
    modifier: Modifier = Modifier,
    roadMap: RoadMap,
    onClickRoadMap: (RoadMap) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .padding(5.dp)
            .clickable { onClickRoadMap(roadMap) },
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
                    painter = painterResource(id = roadMap.verificationStatus.iconResId),
                    contentDescription = null,
                    modifier = Modifier
                        .height(with(LocalDensity.current) { 25.sp.toDp() })
                        .padding(end = 5.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = roadMap.name,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(text = roadMap.description, fontSize = 16.sp)
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(roadMap.nodes) { node ->
                    AssistChip(
                        modifier = Modifier.padding(horizontal = 6.dp),
                        onClick = {},
                        label = { Text(node.name) }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = roadMap.likes.toString(),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Icon(
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = null,
                    modifier = Modifier
                        .height(with(LocalDensity.current) { 16.sp.toDp() }),
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = roadMap.likes.toString(),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Icon(
                    painterResource(id = R.drawable.thumb_down),
                    contentDescription = null,
                    modifier = Modifier
                        .height(with(LocalDensity.current) { 16.sp.toDp() }),
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }


}



@Preview
@Composable
fun RoadMapItemPreview() {
    RoadMapItem(
        modifier = Modifier
            .width(420.dp),
        roadMap = SampleData.roadMap,
        onClickRoadMap = { },
    )
}


