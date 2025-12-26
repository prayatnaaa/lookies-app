package com.prayatna.lookiesapp.presentation.partner.participantList.state

import com.prayatna.lookiesapp.domain.model.EventParticipant

data class ParticipantListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val participants: List<EventParticipant> = emptyList()
)
