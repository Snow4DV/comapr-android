package ru.snowadv.comapr.core.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.FlowCollector
import retrofit2.HttpException
import ru.snowadv.comapr.R
import ru.snowadv.comapr.domain.model.RoadMap


suspend fun <T> FlowCollector<T>.safeApiCall(
    block: suspend FlowCollector<T>.() -> Unit,
    onException: suspend FlowCollector<T>.(message: String) -> Unit
) {
    try {
        block()
    } catch (e: HttpException) {
        onException(e.response()?.message() ?: "Strange error")
    } catch(e: Exception) {
        onException(e.message ?: "Very strange error")
    }
}
