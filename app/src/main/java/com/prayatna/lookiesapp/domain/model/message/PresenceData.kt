package com.prayatna.lookiesapp.domain.model.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresenceData(
    @SerialName("user_id") 
    val userId: String,
    @SerialName("online_at")
    val onlineAt: Long = System.currentTimeMillis()
)
