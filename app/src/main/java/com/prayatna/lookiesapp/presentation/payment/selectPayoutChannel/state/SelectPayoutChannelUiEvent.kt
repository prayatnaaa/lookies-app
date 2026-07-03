package com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel.state

sealed interface SelectPayoutChannelUiEvent {
    data object LoadChannels : SelectPayoutChannelUiEvent
    data class OnSearchQueryChange(val query: String) : SelectPayoutChannelUiEvent
    data object OnBackClicked : SelectPayoutChannelUiEvent
}
