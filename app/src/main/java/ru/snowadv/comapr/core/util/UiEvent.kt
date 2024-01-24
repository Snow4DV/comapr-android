package ru.snowadv.comapr.core.util

sealed class UiEvent {
    class ShowSnackbar(val message: String): UiEvent()
}
