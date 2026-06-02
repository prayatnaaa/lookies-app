package com.prayatna.lookiesapp.presentation.publicMerchantProfile.state

sealed interface PublicMerchantProfileEvent {
    data object OnBackClicked : PublicMerchantProfileEvent
    data class OnEventClicked(val eventId: String) : PublicMerchantProfileEvent
}
