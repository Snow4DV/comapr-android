package ru.snowadv.comapr.presentation.screen.roadmap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.snowadv.comapr.R
import ru.snowadv.comapr.domain.model.Node
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.domain.model.Task
import ru.snowadv.comapr.presentation.view_model.MainViewModel
import ru.snowadv.comapr.presentation.view_model.RoadMapsViewModel
import ru.snowadv.comaprbackend.dto.CategorizedRoadMaps

@Composable
fun RoadMapsScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    roadMapsViewModel: RoadMapsViewModel = hiltViewModel()
) {

    val state = roadMapsViewModel.state

    LaunchedEffect(true) {
        roadMapsViewModel.getRoadMaps()
    }

    RoadMapsCategorizedList(
        categorizedRoadMaps = state.value.roadMaps,
        onClickRoadMap = {}
    )


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
                RoadMapCategoryHeader(
                    modifier = Modifier.clickable {
                        categoryToHidden[categorizedMaps.categoryId] =
                            !(categoryToHidden[categorizedMaps.categoryId] ?: false)
                    },
                    text = categorizedMaps.categoryName
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
private fun RoadMapCategoryHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(10.dp)
    )
}

@Composable
fun RoadMapItem(
    modifier: Modifier = Modifier,
    roadMap: RoadMap,
    onClickRoadMap: (RoadMap) -> Unit
) {
    ElevatedCard(
        modifier = modifier.padding(5.dp),
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
fun CategoryHeaderPreview() {
    RoadMapCategoryHeader(
        modifier = Modifier
            .width(420.dp),
        text = "Competitive programming"
    )
}

@Preview
@Composable
fun RoadMapItemPreview() {
    RoadMapItem(
        modifier = Modifier
            .width(420.dp),
        roadMap = RoadMap(
            1,
            "Neetcode map",
            "Famous neetcode 75 tasks roadmap",
            1,
            listOf(
                Node(
                    1,
                    "Arrays",
                    "Very hard tasks",
                    listOf(
                        Task(1, "Sort array", "Sort wih O(n^2)", "leetcode.com/sometask"),
                        Task(
                            2,
                            "Reverse array",
                            "Reverse array while using O(1) memory",
                            "leetcode.com/sometask"
                        )
                    )
                ),
                Node(
                    2,
                    "Two pointers",
                    "Ranking up",
                    listOf(
                        Task(
                            1,
                            "Fill mountain with max water",
                            "O(n^2) is not accepted",
                            "leetcode.com/sometask"
                        ),
                        Task(
                            2,
                            "Merge two sorted arrays",
                            "Do it in O(n)",
                            "leetcode.com/sometask"
                        )
                    )
                ),
                Node(
                    3,
                    "Some empty node",
                    "Why is it empty?",
                    emptyList()
                ),
                Node(
                    4,
                    "Also empty node",
                    "What is going on?",
                    emptyList()
                )
            ),
            likes = 5,
            dislikes = 4,
            categoryName = "Competitive programming",
            verificationStatus = RoadMap.VerificationStatus.VERIFIED
        ),
        onClickRoadMap = { },
    )
}