package com.prayatna.lookiesapp.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun Uri.readFileBytes(context: Context): ByteArray? = withContext(Dispatchers.IO) {
    try {
        context.contentResolver.openInputStream(this@readFileBytes)?.use { it.readBytes() }
    } catch (e: Exception) {
        null
    }
}

fun Uri.getMimeType(context: Context): String? {
    return context.contentResolver.getType(this)
}

fun String.getExtensionFromMimeType(): String {
    return when (this) {
        "application/pdf" -> "pdf"
        "image/png" -> "png"
        "image/jpeg" -> "jpg"
        "image/webp" -> "webp"
        else -> "bin"
    }
}
