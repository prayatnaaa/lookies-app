package com.prayatna.lookiesapp.presentation.partner.main.home.state

sealed interface PartnerHomeEffect {
    data class NavigateToChat(val merchantId: String): PartnerHomeEffect
    data object NavigateBack : PartnerHomeEffect
    data object NavigateCreateEvent : PartnerHomeEffect
    data object NavigateMyEvents : PartnerHomeEffect
    data object NavigateRefund : PartnerHomeEffect
    data object NavigatePainting : PartnerHomeEffect
    data object NavigateShipment : PartnerHomeEffect
    data object NavigateMonthlyFinanceList : PartnerHomeEffect
    data class NavigateMemberList(val merchantBusinessId: String): PartnerHomeEffect

    data class ShowMessage(val message: String) : PartnerHomeEffect
    data class NavigateEditProfile(val businessId: String): PartnerHomeEffect
}