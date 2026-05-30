package com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel.state

import com.prayatna.lookiesapp.domain.model.payment.PayoutChannel

data class SelectPayoutChannelUiState(
    val isLoading: Boolean = false,
    val channels: List<PayoutChannel> = emptyList(),
    val filteredChannels: List<PayoutChannel> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null
)
