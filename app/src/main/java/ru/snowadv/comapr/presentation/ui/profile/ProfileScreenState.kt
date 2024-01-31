package ru.snowadv.comapr.presentation.ui.profile

import ru.snowadv.comapr.domain.model.UserAndSessions

data class ProfileScreenState(
    val data: UserAndSessions? = null,
    val loading: Boolean = false
)