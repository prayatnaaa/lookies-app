package com.prayatna.lookiesapp.data.remote.dto.response.base

import kotlinx.serialization.Serializable

@Serializable
data class RpcBaseResponse(
    val message: String
)