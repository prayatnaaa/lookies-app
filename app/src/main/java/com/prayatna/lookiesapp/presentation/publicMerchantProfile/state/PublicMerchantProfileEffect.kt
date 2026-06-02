package com.prayatna.lookiesapp.presentation.publicMerchantProfile.state

sealed interface PublicMerchantProfileEffect {
    data object NavigateBack : PublicMerchantProfileEffect
    data class NavigateToEventDetail(val eventId: String) : PublicMerchantProfileEffect
}
