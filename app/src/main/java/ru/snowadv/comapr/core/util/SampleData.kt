package ru.snowadv.comapr.core.util

import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.model.Node
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.domain.model.Role
import ru.snowadv.comapr.domain.model.SessionChatMessage
import ru.snowadv.comapr.domain.model.Task
import ru.snowadv.comapr.domain.model.User
import ru.snowadv.comapr.domain.model.UserMapCompletionState
import java.time.LocalDateTime

object SampleData {
    val roadMap = RoadMap(
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
                        "Rain water or smth",
                        "idk i'm not a geologist",
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
    )

    val user = User(1, "testuser", "email@example.com", Role.ROLE_ADMIN)

    val date = LocalDateTime.of(2024, 1, 1, 10, 0 , 0)

    val messages = listOf(SessionChatMessage(1, user, date, "hi!"), SessionChatMessage(1, user, date, "how are you?"))

    val session = MapSession(
        id = 1,
        creator = user,
        users = listOf(UserMapCompletionState(1, user, setOf(2))),
        public = true,
        startDate = date,
        state = MapSession.State.LOBBY,
        groupChatUrl = "tg.com/somechat",
        messages = messages,
        roadMap = roadMap
    )
}

