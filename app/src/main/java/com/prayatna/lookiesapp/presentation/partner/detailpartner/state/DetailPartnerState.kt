package com.prayatna.lookiesapp.presentation.partner.detailpartner.state

data class DetailPartnerState(
    val isLoading: Boolean = false,
    val data: DetailPartnerUiModel? = null,
    val error: String? = null
)
