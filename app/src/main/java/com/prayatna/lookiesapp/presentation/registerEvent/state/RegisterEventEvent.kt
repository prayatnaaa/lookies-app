package com.prayatna.lookiesapp.presentation.registerEvent.state

sealed interface RegisterEventEvent {
    data class SetEventId(val id: Int) : RegisterEventEvent
    data class SetFee(val fee: Double) : RegisterEventEvent
    data class SetMerchantId(val merchantId: String) : RegisterEventEvent
    data class SetMaxLimit(val maxLimit: Int) : RegisterEventEvent
    
    data object NextStep : RegisterEventEvent
    data object PrevStep : RegisterEventEvent
    data class TogglePainting(val id: Int) : RegisterEventEvent
    data object Submit : RegisterEventEvent
    data object DismissError : RegisterEventEvent

    // Interactive Commission Proposal
    data class SetProposedCommission(val rate: Float) : RegisterEventEvent
}
