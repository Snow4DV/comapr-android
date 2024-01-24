package ru.snowadv.comapr.core.util

sealed class NavigationEvent(val route: String) {
    class ToHomeScreen(): NavigationEvent("home")
    class ToLoginScreen(): NavigationEvent("login")

    class ToRoadMap(val mapId: Long): NavigationEvent("roadmap")
    class ToSession(val sessionId: Long): NavigationEvent("session")

}