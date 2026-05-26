package com.prayatna.lookiesapp.presentation.merchant.merchantMemberByMerchantId.state

import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember

data class MerchantMemberByMerchantIdUiState(
    val isLoading: Boolean = false,
    val merchantMembers: List<MerchantMember> = emptyList(),
    val errorMessage: String? = null
)
