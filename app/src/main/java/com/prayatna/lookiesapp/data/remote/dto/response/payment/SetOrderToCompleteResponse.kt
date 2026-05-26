package com.prayatna.lookiesapp.data.remote.dto.response.payment

import kotlinx.serialization.Serializable

@Serializable
data class SetOrderToCompleteResponse(
    val message: String,
    val status: String
)