package com.prayatna.lookiesapp.utils

import org.json.JSONObject

fun extractSupabaseError(raw: String): String {
    return try {
        val json = JSONObject(raw)

        json.optString("msg")
            .ifBlank { json.optString("message") }
            .ifBlank { json.optString("error") }
            .ifBlank { raw }
    } catch (e: Exception) {
        raw.substringAfter("error:", raw).trim()
    }
}

fun mapUpdateErrorToMessage(e: Throwable): String {
    val message = e.message ?: return "Something went wrong"

    return when {
        message.contains("events_max_participant_positive") ->
            "Maximum participants must be greater than 0"

        message.contains("events_max_painting_positive") ->
            "Maximum paintings must be greater than 0"

        message.contains("events_max_painting_per_artist_positive") ->
            "Max paintings per artist must be greater than 0"

        message.contains("violates not-null constraint") ->
            "Required fields are missing"

        message.contains("duplicate key") ->
            "Event already exists"

        else -> "Something went wrong, please try again"
    }
}
