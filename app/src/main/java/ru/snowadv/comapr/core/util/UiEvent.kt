package ru.snowadv.comapr.core.util

sealed class UiEvent {
    class ShowSnackbar(val message: String): UiEvent()
    class Navigate(val destination: String): UiEvent()
}
