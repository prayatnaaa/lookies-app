package com.prayatna.lookiesapp.utils

import com.prayatna.lookiesapp.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

object Helper {
    fun buildImageUrl(imageName: String, bucketName: String) =
        "${BuildConfig.BASE_URL}/storage/v1/object/public/${bucketName}/${imageName}"

    fun parseDateRange(input: String): String {
        val parts = input.split(" - ")
        if (parts.size != 2) return input

        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()

        val start = isoFormat.parse(parts[0])
        val end = isoFormat.parse(parts[1])

        return "${start?.let { outputFormat.format(it) }} - ${end?.let { outputFormat.format(it) }}"
    }

    fun formatIdr(value: String): String {
        val number = value.toLongOrNull() ?: return ""
        return "Rp " + number
            .toString()
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
    }
}