package ru.snowadv.comapr.presentation.ui.roadmap.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.snowadv.comapr.R
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.domain.model.Task
import ru.snowadv.comapr.presentation.ui.common.GroupHeader
import ru.snowadv.comapr.presentation.ui.common.SimpleTopBarWithBackButton

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun RoadMapAndOrSessionScreenContent(
    modifier: Modifier = Modifier,
    isSession: Boolean = false,
    onBackClicked: () -> Unit,
    onRefresh: () -> Unit,
    onUrlClick: (String) -> Unit,
    roadMap: RoadMap?,
    loading: Boolean,
    sessionComposable: @Composable (() -> Unit)? = null,
    usersComposable: @Composable (() -> Unit)? = null,
    sessionStatsComposable: @Composable (() -> Unit)? = null,
    onTaskChecked: ((Task, Boolean) -> Unit)? = null,
    taskStates: Set<Long> = emptySet(),
    onCreateSession: () -> Unit = {},
    scrollToNodeId: Long? = null,
    participantsCount: Int = 0
) {
    val lazyListState = rememberLazyListState()

    val pullRefreshState = rememberPullRefreshState(loading, { onRefresh() })

    val hiddenNodes = remember { mutableStateMapOf<Long, Boolean>() }

    val mapVisibilityState = remember { mutableStateOf(true) }
    val sessionComposableVisibilityState = remember { mutableStateOf(true) }
    val usersComposableVisibilityState = remember { mutableStateOf(true) }
    val sessionStatsComposableVisibilityState = remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(scrollToNodeId, roadMap) {
        scrollToNodeId?.let { nodeId ->
            coroutineScope.launch {
                var flag = true
                roadMap?.nodes?.filter {
                    if (it.id == nodeId) {
                        flag = false
                    }
                    flag
                }?.sumOf { it.tasks.size + 1 }?.let { sum ->
                    lazyListState.animateScrollToItem(sum + 2)
                }

            }
        }
    }

    Scaffold(modifier = modifier, topBar = {
        SimpleTopBarWithBackButton(
            title = roadMap?.name ?: stringResource(R.string.loading),
            onBackClicked = onBackClicked
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .padding(paddingValues)
                .fillMaxWidth()
        ) {

            if (roadMap != null) {
                LazyColumn(
                    state = lazyListState, modifier = Modifier.fillMaxWidth()
                ) {
                    stickyHeader {
                        GroupHeader(
                            modifier = Modifier.clickable {
                                mapVisibilityState.value = !(mapVisibilityState.value)
                            }, title = roadMap.name
                        )
                    }
                    item {
                        AnimatedVisibility(visible = mapVisibilityState.value) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Text(
                                            text = stringResource(R.string.verification_status),
                                            fontSize = 20.sp
                                        )
                                        Icon(
                                            painter = painterResource(id = roadMap.verificationStatus.iconResId),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .height(with(LocalDensity.current) { 18.sp.toDp() })
                                                .padding(end = 5.dp),
                                            tint = MaterialTheme.colorScheme.primary,
                                        )

                                    }
                                    Text(text = roadMap.description, fontSize = 18.sp)

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
                                            modifier = Modifier.height(with(LocalDensity.current) { 16.sp.toDp() }),
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
                                            modifier = Modifier.height(with(LocalDensity.current) { 16.sp.toDp() }),
                                            tint = MaterialTheme.colorScheme.tertiary,
                                        )
                                    }
                                    if (!isSession) {
                                        Row(modifier = Modifier.clickable {
                                            onCreateSession()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Create,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .height(with(LocalDensity.current) { 18.sp.toDp() })
                                                    .padding(end = 5.dp),
                                                tint = MaterialTheme.colorScheme.primary,
                                            )
                                            Text(
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                text = stringResource(id = R.string.create_session),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    sessionComposable?.let { sessionComposable ->
                        stickyHeader {
                            GroupHeader(
                                modifier = Modifier.clickable {
                                    sessionComposableVisibilityState.value =
                                        !(sessionComposableVisibilityState.value)
                                }, title = stringResource(R.string.current_session)
                            )
                        }
                        item {
                            AnimatedVisibility(visible = sessionComposableVisibilityState.value) {
                                sessionComposable()
                            }
                        }
                    }

                    usersComposable?.let { usersComposable ->
                        stickyHeader {
                            GroupHeader(
                                modifier = Modifier.clickable {
                                    usersComposableVisibilityState.value =
                                        !(usersComposableVisibilityState.value)
                                }, title = stringResource(R.string.companions)
                            )
                        }
                        item {
                            AnimatedVisibility(visible = usersComposableVisibilityState.value) {
                                usersComposable()
                            }
                        }
                    }

                    sessionStatsComposable?.let { sessionStatsComposable ->
                        stickyHeader {
                            GroupHeader(
                                modifier = Modifier.clickable {
                                    sessionStatsComposableVisibilityState.value =
                                        !(sessionStatsComposableVisibilityState.value)
                                }, title = stringResource(R.string.statistics)
                            )
                        }
                        item {
                            AnimatedVisibility(visible = sessionStatsComposableVisibilityState.value) {
                                sessionStatsComposable()
                            }
                        }
                    }

                    roadMap.nodes.forEach { node ->
                        stickyHeader {
                            GroupHeader(
                                modifier = Modifier.clickable {
                                    hiddenNodes[node.id] = !(hiddenNodes[node.id] ?: true)
                                }, title = node.name, description = node.description
                            )
                        }
                        items(node.tasks) { task ->
                            AnimatedVisibility(
                                visible = hiddenNodes[node.id] ?: true
                            ) {
                                Card(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth(),
                                ) {
                                    Row(
                                        Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1.0f, fill = true)) {
                                            Text(
                                                text = task.name,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(text = task.description, fontSize = 15.sp)

                                            val urlBeginPattern =
                                                remember { Regex("""(http://)|(https://)""") }
                                            task.url?.let { taskUrl ->
                                                val resLink = taskUrl.replace(urlBeginPattern, "")
                                                Row(modifier = Modifier.clickable {
                                                    onUrlClick(taskUrl)
                                                }) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.link_filled),
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .height(with(LocalDensity.current) { 18.sp.toDp() })
                                                            .padding(end = 5.dp),
                                                        tint = MaterialTheme.colorScheme.primary,
                                                    )
                                                    Text(
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        text = resLink,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }

                                            val finishedPartCount = task.finishedUserIds?.size ?: 0
                                            if (finishedPartCount in 1..<participantsCount) {

                                                Text(
                                                    text = "${task.finishedUserIds?.size}/${participantsCount}${
                                                        stringResource(R.string.already_finished)
                                                    }",
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            } else if(finishedPartCount >= participantsCount) {
                                                Text(
                                                    text = stringResource(R.string.everyone_finished_yay),
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }

                                        }
                                        onTaskChecked?.let {
                                            Checkbox(checked = task.id in taskStates,
                                                onCheckedChange = {
                                                    onTaskChecked(
                                                        task, !(task.id in taskStates)
                                                    )
                                                })
                                        }

                                    }
                                }
                            }

                        }

                    }
                }


            }
            PullRefreshIndicator(
                refreshing = loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }


    }
}
