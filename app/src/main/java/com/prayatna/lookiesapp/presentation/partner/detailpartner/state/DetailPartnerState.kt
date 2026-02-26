package com.prayatna.lookiesapp.presentation.partner.detailpartner.state

import com.prayatna.lookiesapp.domain.model.user.MerchantBusiness

data class DetailPartnerState(
    val isLoading: Boolean = false,
    val data: MerchantBusiness? = null,
    val error: String? = null
)
