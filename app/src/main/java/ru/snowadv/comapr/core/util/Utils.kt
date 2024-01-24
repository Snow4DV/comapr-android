package ru.snowadv.comapr.core.util

import kotlinx.coroutines.flow.FlowCollector
import retrofit2.HttpException


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
