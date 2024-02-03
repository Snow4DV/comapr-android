package ru.snowadv.comapr.core.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.FlowCollector
import retrofit2.HttpException
import ru.snowadv.comapr.R
import ru.snowadv.comapr.domain.model.Category
import ru.snowadv.comapr.domain.model.RoadMap
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.TimeZone


suspend fun <T> FlowCollector<T>.safeApiCall(
    block: suspend FlowCollector<T>.() -> Unit,
    onException: suspend FlowCollector<T>.(message: String) -> Unit
) {
    try {
        block()
    } catch (e: HttpException) {
        onException(e.response()?.errorBody()?.string() ?: "Strange error")
    } catch(e: Exception) {
        onException(e.message ?: "Very strange error")
    }
}

fun LocalDateTime.toZonedDateTimeWithCurrentZone(): ZonedDateTime {
    return this.atZone(getCurrentZoneId())
}

fun getCurrentZoneId(): ZoneId {
    return TimeZone.getDefault().toZoneId()
}
fun ZonedDateTime.toUtcLocalDateTime(): LocalDateTime {
    return this.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
}

fun getUrlPath(path: String, args: List<Pair<String, String>>): String {
    if(args.isEmpty()) return path
    return "$path?" + args.joinToString("&") { "${it.first}=${it.second}" }
}

fun millisToZonedDateTime(millis: Long): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), getCurrentZoneId())
}
@Composable
fun Category.toDisplayName(): String {
    return when(this.name) {
        "VERIFIED" -> stringResource(R.string.verified)
        "COMMUNITY_CHOICE" -> stringResource(R.string.community_choice)
        "UNVERIFIED" -> stringResource(R.string.unverified)
        "HIDDEN" -> stringResource(R.string.hidden)
        else -> this.name
    }
}
