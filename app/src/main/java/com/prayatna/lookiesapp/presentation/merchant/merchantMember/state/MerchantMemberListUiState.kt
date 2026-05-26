package com.prayatna.lookiesapp.presentation.merchant.merchantMember.state

import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember

data class MerchantMemberListUiState (
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val merchantMembers: List<MerchantMember> = emptyList()
)