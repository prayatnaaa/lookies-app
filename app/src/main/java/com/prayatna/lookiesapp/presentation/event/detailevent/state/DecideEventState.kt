package com.prayatna.lookiesapp.presentation.event.detailevent.state

import com.prayatna.lookiesapp.domain.model.admin.DecideEventResult

data class DecideEventState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: DecideEventResult? = null
)