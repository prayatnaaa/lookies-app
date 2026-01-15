package com.prayatna.lookiesapp.utils

import com.prayatna.lookiesapp.BuildConfig

object Helper {
    fun buildImageUrl(imageName: String, bucketName: String) =
        "${BuildConfig.BASE_URL}/storage/v1/object/public/${bucketName}/${imageName}"

    fun defaultAvatar(data: String): String {
        return "https://ui-avatars.com/api/?name=${data}"
    }
}