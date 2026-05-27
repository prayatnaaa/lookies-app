package com.prayatna.lookiesapp.presentation.merchant.acceptPartnerInvitation.state

import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember

data class AcceptPartnerInvitationUiState(
    val isLoading: Boolean = true,
    val isAccepting: Boolean = false,
    val isRejecting: Boolean = false,
    val invitation: MerchantMember? = null,
    val errorMessage: String? = null
)
