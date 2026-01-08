package com.prayatna.lookiesapp.presentation.partner.manageEvent.state

import com.prayatna.lookiesapp.domain.model.event.Event

data class PartnerManageEventUiState(
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val event: Event? = null,
)