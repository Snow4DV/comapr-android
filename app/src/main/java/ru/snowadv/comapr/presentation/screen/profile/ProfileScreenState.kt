package ru.snowadv.comapr.presentation.screen.profile

import ru.snowadv.comapr.domain.model.AuthUser
import ru.snowadv.comapr.domain.model.Category
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.domain.model.UserAndSessions
import ru.snowadv.comaprbackend.dto.CategorizedRoadMaps
import kotlin.enums.EnumEntries

data class ProfileScreenState(
    val data: UserAndSessions? = null,
    val loading: Boolean = false
)