package com.prayatna.lookiesapp.data.remote.response.base

import kotlinx.serialization.Serializable

@Serializable
data class RpcBaseResponse(
    val message: String
)