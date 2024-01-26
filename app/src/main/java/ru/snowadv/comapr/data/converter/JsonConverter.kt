package ru.snowadv.comapr.data.converter

import java.lang.reflect.Type

interface JsonConverter {
    fun <T> fromJson(json: String, type: Type): T?
    fun <T> toJson(obj: T): String?
}