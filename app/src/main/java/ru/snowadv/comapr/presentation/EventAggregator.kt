package ru.snowadv.comapr.presentation

import kotlinx.coroutines.channels.Channel
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.UiEvent

interface EventAggregator {
    val eventChannel: Channel<UiEvent>
    val navigationChannel: Channel<NavigationEvent>
}