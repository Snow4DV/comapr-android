package ru.snowadv.comapr.domain.model

data class UserAndSessions(
    val user: User,
    val sessions: List<MapSession>
)