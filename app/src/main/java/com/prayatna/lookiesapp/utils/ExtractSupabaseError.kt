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
