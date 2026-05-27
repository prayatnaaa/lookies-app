package com.prayatna.lookiesapp.data.remote.dto.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AcceptInvitationRequest(
    @SerialName("p_merchant_account_id")
    val merchantAccountId: String
)