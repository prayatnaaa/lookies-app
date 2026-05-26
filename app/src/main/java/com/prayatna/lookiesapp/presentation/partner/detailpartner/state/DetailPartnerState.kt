package com.prayatna.lookiesapp.presentation.partner.detailpartner.state

import com.prayatna.lookiesapp.domain.model.merchant.MerchantDetail

data class DetailPartnerState(
    val isLoading: Boolean = false,
    val data: MerchantDetail? = null,
    val error: String? = null
)
