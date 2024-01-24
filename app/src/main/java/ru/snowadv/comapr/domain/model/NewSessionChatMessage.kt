package ru.snowadv.comapr.domain.model

import ru.snowadv.comapr.data.remote.dto.NewSessionChatMessageDto

data class NewSessionChatMessage(
    val text: String
) {
    fun toDto(): NewSessionChatMessageDto {
        return NewSessionChatMessageDto(text)
    }
}