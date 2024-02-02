package ru.snowadv.comapr.core.util

sealed class NavigationEvent(val route: String) {
    data object ToHomeScreen: NavigationEvent("home")
    data object BackToHomeScreen: NavigationEvent("home")
    data object ToLoginScreen: NavigationEvent("login")

    class ToRoadMap(mapId: Long): NavigationEvent("roadmap/$mapId")
    class ToSession(sessionId: Long): NavigationEvent("session/$sessionId")
    class ToSessionEditor(sessionId: Long? = null): NavigationEvent(
        ("sessionEditor${sessionId?.let { "?sessionId={$it}" } ?: ""}")
    )



}