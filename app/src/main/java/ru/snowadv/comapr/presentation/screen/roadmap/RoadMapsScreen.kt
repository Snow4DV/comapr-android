package ru.snowadv.comapr.presentation.screen.roadmap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.snowadv.comapr.domain.model.Node
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.domain.model.Task
import ru.snowadv.comapr.presentation.screen.login.AuthScreenContent
import ru.snowadv.comapr.presentation.view_model.MainViewModel

@Composable
fun RoadMapsScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {

}

@Composable
fun RoadMapItem(
    modifier: Modifier = Modifier,
    roadMap: RoadMap,
    onClickRoadMap: (RoadMap) -> Unit
) {
    Column(modifier = modifier.padding(5.dp)) {
        Text(text = roadMap.name, fontSize = 25.sp, )
    }
}


@Preview
@Composable
fun RoadMapItemPreview() {
    RoadMapItem(
        modifier = Modifier
            .width(420.dp)
            .height(905.dp),
        roadMap = RoadMap(
            1,
            "Interesting map",
            "You should check it out",
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
                )
            ),
            likes = 5,
            dislikes = 4,
            categoryName = "Competitive programming",
            verificationStatus = RoadMap.VerificationStatus.VERIFIED
        ),
        onClickRoadMap = { }
    )
}