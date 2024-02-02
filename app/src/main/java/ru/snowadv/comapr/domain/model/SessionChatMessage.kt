package ru.snowadv.comapr.domain.model

import java.time.LocalDateTime
import java.time.ZonedDateTime


data class SessionChatMessage(
    val id: Long,
    val creator: User,
    val timestamp: ZonedDateTime,
    val text: String
)