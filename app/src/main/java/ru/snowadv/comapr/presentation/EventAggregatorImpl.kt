package ru.snowadv.comapr.presentation

import kotlinx.coroutines.channels.Channel
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.UiEvent

class EventAggregatorImpl : EventAggregator {
    override val eventChannel: Channel<UiEvent> = Channel()
    override val navigationChannel: Channel<NavigationEvent> = Channel()
}