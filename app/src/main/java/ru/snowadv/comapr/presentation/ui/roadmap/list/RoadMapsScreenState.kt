package ru.snowadv.comapr.presentation.ui.roadmap.list

import ru.snowadv.comapr.domain.model.Category
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comaprbackend.dto.CategorizedRoadMaps

data class RoadMapsScreenState(
    val roadMaps: List<CategorizedRoadMaps> = emptyList(),
    val loading: Boolean = false,
    val filterStatusId: Int? = null,
    val filterCategoryId: Long? = null
) {

    fun getCategories(): List<Category> {
        val categories: MutableSet<Category> = mutableSetOf()
        roadMaps.forEach { categorizedMaps ->
            categorizedMaps.roadMaps.forEach {
                categories.add(Category(it.categoryId, it.categoryName))
            }
        }
        return categories.sortedBy { it.name }
    }

    companion object {
        fun getVerificationStatuses(): List<RoadMap.VerificationStatus> {
            return RoadMap.VerificationStatus.entries.reversed()
        }
    }
}