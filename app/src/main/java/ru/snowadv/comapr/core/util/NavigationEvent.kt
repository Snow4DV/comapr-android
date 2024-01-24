package ru.snowadv.comapr.core.util

sealed class NavigationEvent(val route: String) {
    class ToHomeScreen(): NavigationEvent("home")
    class ToLoginScreen(): NavigationEvent("login")

}