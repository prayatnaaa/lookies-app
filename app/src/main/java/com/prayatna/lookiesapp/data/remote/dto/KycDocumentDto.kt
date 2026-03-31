package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetKycDocumentDto (
    val id: String,
    @SerialName("business_id")
    val businessId: String,
    val type: String,
    val country: String,
    @SerialName("file_id")
    val fileId: String,
    @SerialName("created_at")
    val createdAt: String
)