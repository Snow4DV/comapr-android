package ru.snowadv.comapr.core.util

sealed class NavigationEvent(val route: String) {
    class ToHomeScreen(): NavigationEvent("home")
    class ToLoginScreen(): NavigationEvent("login")

    class ToRoadMap(mapId: Long): NavigationEvent("roadmap/$mapId")
    class ToSession(sessionId: Long): NavigationEvent("session/$sessionId")



}