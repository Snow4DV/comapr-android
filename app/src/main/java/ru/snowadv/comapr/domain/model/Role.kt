package ru.snowadv.comapr.domain.model

import ru.snowadv.comapr.R

enum class Role(val displayNameResId: Int) {
    ROLE_USER(R.string.user),
    ROLE_MODERATOR(R.string.moderator),
    ROLE_ADMIN(R.string.administrator),
    UNDEFINED(R.string.undefined)
}