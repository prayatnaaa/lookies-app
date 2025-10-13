package com.prayatna.lookiesapp.utils

import com.prayatna.lookiesapp.BuildConfig

object Helper {
    fun buildImageUrl(imageName: String) =
        "${BuildConfig.BASE_URL}/storage/v1/object/public/${imageName}"
}