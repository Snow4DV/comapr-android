package ru.snowadv.comapr.data.converter

import com.google.gson.Gson
import java.lang.reflect.Type

class JsonConverterImpl(
    private val gson: Gson
): JsonConverter {
    override fun <T> fromJson(json: String, type: Type): T? {
        return gson.fromJson(json, type)
    }
    override fun <T> toJson(obj: T): String? {
        return gson.toJson(obj)
    }
}