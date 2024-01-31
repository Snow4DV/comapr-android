package ru.snowadv.comapr.presentation.ui.common

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
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.snowadv.comapr.R
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.domain.model.Task

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun RoadMapAndOrSessionScreenContent(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onRefresh: () -> Unit,
    onUrlClick: (String) -> Unit,
    roadMap: RoadMap?,
    loading: Boolean,
    sessionComposable: @Composable (() -> Unit)? = null,
    onTaskChecked: ((Task) -> Unit)? = null,
    taskStates: Set<Long> = emptySet()
) {
    val pullRefreshState =
        rememberPullRefreshState(loading, { onRefresh() })

    val hiddenNodes = remember { mutableStateMapOf<Long, Boolean>() }

    val mapVisibilityState = remember { mutableStateOf(true) }
    val sessionComposableVisibilityState = remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(roadMap?.name ?: stringResource(R.string.loading)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .padding(paddingValues)
                .fillMaxWidth()
        ) {

            if (roadMap != null) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    stickyHeader {
                        GroupHeader(
                            modifier = Modifier.clickable {
                                mapVisibilityState.value = !(mapVisibilityState.value)
                            },
                            title = roadMap.name
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
                    }

                    sessionComposable?.let { sessionComposable ->
                        stickyHeader {
                            GroupHeader(
                                modifier = Modifier.clickable {
                                    sessionComposableVisibilityState.value = !(sessionComposableVisibilityState.value)
                                },
                                title = stringResource(R.string.current_session)
                            )
                        }
                        item {
                            AnimatedVisibility(visible = sessionComposableVisibilityState.value) {
                                sessionComposable()
                            }
                        }
                    }

                    roadMap.nodes.forEach { node ->
                        stickyHeader {
                            GroupHeader(
                                modifier = Modifier.clickable {
                                    hiddenNodes[node.id] =
                                        !(hiddenNodes[node.id] ?: true)
                                },
                                title = node.name,
                                description = node.description
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
                                            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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
                                                Row(
                                                    modifier = Modifier.clickable {
                                                        onUrlClick(taskUrl)
                                                    }
                                                ) {
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

                                        }
                                        onTaskChecked?.let {
                                            Checkbox(
                                                checked = task.id in taskStates,
                                                onCheckedChange = {onTaskChecked(task)}
                                            )
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
