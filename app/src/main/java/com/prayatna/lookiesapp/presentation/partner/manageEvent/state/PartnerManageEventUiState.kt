package com.prayatna.lookiesapp.presentation.partner.manageEvent.state

import com.prayatna.lookiesapp.data.remote.dto.EventStatisticDto
import com.prayatna.lookiesapp.domain.model.event.Event

data class PartnerManageEventUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val event: Event? = null,
    val statistics: EventStatisticDto? = null
)