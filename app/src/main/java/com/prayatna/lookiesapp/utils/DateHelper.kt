package com.prayatna.lookiesapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId



fun formatDate(dateString: String): String {
        return try {
            // Asumsi format backend: "2023-10-25" atau "2023-10-25T10:00:00Z"
            // Kita gunakan SimpleDateFormat (Legacy)
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            // Opsional: Jika backend kirim UTC, set timezone input ke UTC
             inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val date: Date? = inputFormat.parse(dateString)

            if (date != null) {
                val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                outputFormat.format(date)
            } else {
                dateString
            }
        } catch (e: Exception) {
            e.printStackTrace()
            dateString // Jika gagal parsing, kembalikan string aslinya
        }
    }

fun formatChatTime(raw: String): String {
    return try {
        val parsed = OffsetDateTime.parse(raw)
            .atZoneSameInstant(ZoneId.systemDefault())

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        parsed.format(formatter)
    } catch (e: Exception) {
        raw
    }
}