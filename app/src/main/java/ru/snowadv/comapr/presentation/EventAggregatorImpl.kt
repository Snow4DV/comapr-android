package ru.snowadv.comapr.presentation

import kotlinx.coroutines.channels.Channel
import ru.snowadv.comapr.core.util.UiEvent

class EventAggregatorImpl() : EventAggregator {
    override val eventChannel: Channel<UiEvent> = Channel<UiEvent>()
}