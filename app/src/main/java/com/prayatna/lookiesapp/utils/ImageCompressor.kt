package com.prayatna.lookiesapp.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import kotlinx.coroutines.isActive
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

suspend fun Uri.compressImage(
    context: Context,
    compressionThreshold: Long
): ByteArray? = withContext(Dispatchers.IO) io@{
    val mimeType = context.contentResolver.getType(this@compressImage)
    val inputBytes = context.contentResolver
        .openInputStream(this@compressImage)
        ?.use { it.readBytes() }
        ?: return@io null

    ensureActive()

    return@io withContext(Dispatchers.Default) default@{
        val bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes.size)
            ?: return@default null

        ensureActive()

        val compressFormat = when (mimeType) {
            "image/png" -> Bitmap.CompressFormat.PNG
            "image/jpeg" -> Bitmap.CompressFormat.JPEG
            "image/webp" -> if (Build.VERSION.SDK_INT >= 30)
                Bitmap.CompressFormat.WEBP_LOSSLESS
            else
                @Suppress("DEPRECATION") Bitmap.CompressFormat.WEBP
            else -> Bitmap.CompressFormat.JPEG
        }

        var outputBytes: ByteArray
        var quality = 90

        do {
            ByteArrayOutputStream().use { outputStream ->
                bitmap.compress(compressFormat, quality, outputStream)
                outputBytes = outputStream.toByteArray()
                quality -= (quality * 0.1).roundToInt()
            }
        } while (
            isActive &&
            outputBytes.size > compressionThreshold &&
            quality > 5 &&
            compressFormat != Bitmap.CompressFormat.PNG
        )

        outputBytes
    }
}

