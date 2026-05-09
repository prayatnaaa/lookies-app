package com.prayatna.lookiesapp.presentation.partner.merchantMemberByMerchantId.state

import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember

data class MerchantMemberByMerchantIdUiState(
    val isLoading: Boolean = false,
    val merchantMembers: List<MerchantMember> = emptyList(),
    val errorMessage: String? = null
)
