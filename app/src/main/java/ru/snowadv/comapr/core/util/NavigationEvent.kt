package ru.snowadv.comapr.core.util

sealed class NavigationEvent(val route: String, val popUpToStart: Boolean = false) {
    data object ToHomeScreen : NavigationEvent("home")
    data object PopBackStack : NavigationEvent("home")
    data object ToLoginScreen : NavigationEvent("login")

    class ToRoadMap(mapId: Long, nodeId: Long? = null, popUpToStart: Boolean = false) :
        NavigationEvent(getUrlPath(
            "roadmap",
            listOfNotNull(
                mapId.toString().let { "roadmapId" to it },
                nodeId?.toString()?.let { "nodeId" to it }
            )), popUpToStart)

    class ToSession(sessionId: Long, popUpToStart: Boolean = false) :
        NavigationEvent("session/$sessionId", popUpToStart)

    class ToSessionEditor(sessionId: Long? = null, roadMapId: Long? = null) : NavigationEvent(
        (getUrlPath(
            "sessionEditor",
            listOfNotNull(
                sessionId?.toString()?.let { "sessionId" to it },
                roadMapId?.toString()?.let { "roadMapId" to it }
            )))
    )

    class ToQuizScreen(sessionId: Long, taskId: Long) : NavigationEvent("quiz/$sessionId/$taskId")
}
