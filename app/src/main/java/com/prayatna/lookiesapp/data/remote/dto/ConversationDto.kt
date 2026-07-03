package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConversationDto(

    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("merchant_id")
    val merchantId: String,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

)