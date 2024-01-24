package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.Category


data class CategoryDto(
    val id: Long,
    val name: String
) {
    fun toModel(): Category {
        return Category(id, name)
    }
}