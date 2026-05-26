package com.prayatna.lookiesapp.presentation.partner.main.home.state

sealed interface PartnerHomeEffect {
    data object NavigateBack : PartnerHomeEffect
    data object NavigateCreateEvent : PartnerHomeEffect
    data object NavigateMyEvents : PartnerHomeEffect
    data object NavigateRefund : PartnerHomeEffect
    data object NavigatePainting : PartnerHomeEffect
    data object NavigateShipment : PartnerHomeEffect
    data object NavigateMonthlyFinanceList : PartnerHomeEffect
    data class NavigateMemberList(val merchantBusinessId: String): PartnerHomeEffect

    data class ShowMessage(val message: String) : PartnerHomeEffect
}