package ru.snowadv.comapr.core.util

sealed class NavigationEvent(val route: String, val popUpToStart: Boolean = false) {
    data object ToHomeScreen : NavigationEvent("home")
    data object BackToHomeScreen : NavigationEvent("home")
    data object ToLoginScreen : NavigationEvent("login")

    class ToRoadMap(mapId: Long, popUpToStart: Boolean = false) :
        NavigationEvent("roadmap/$mapId", popUpToStart)

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


}