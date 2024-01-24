package ru.snowadv.comapr.domain.model

import java.time.LocalDateTime


data class SessionChatMessage(
    val id: Long,
    val creator: User,
    val timestamp: LocalDateTime,
    val text: String
)