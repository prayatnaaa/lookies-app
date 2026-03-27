package com.prayatna.lookiesapp.presentation.partner.detailpartner.state

import com.prayatna.lookiesapp.domain.model.admin.DecidePartnerApplicationResult

data class AdminDecideState(
    val isLoading: Boolean = false,
    val success: DecidePartnerApplicationResult? = null,
    val error: String? = null
)