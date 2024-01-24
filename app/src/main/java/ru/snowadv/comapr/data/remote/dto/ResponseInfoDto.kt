package ru.snowadv.comaprbackend.payload.response

import ru.snowadv.comapr.domain.model.ResponseInfo

data class ResponseInfoDto(val message: String) {
    fun toModel(): ResponseInfo {
        return ResponseInfo(message)
    }
}