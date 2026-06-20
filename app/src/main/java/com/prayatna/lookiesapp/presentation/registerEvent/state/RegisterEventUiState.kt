package com.prayatna.lookiesapp.presentation.registerEvent.state

import com.prayatna.lookiesapp.domain.model.artist.RegisterEventOutput
import com.prayatna.lookiesapp.domain.model.event.EventRevenueRules
import com.prayatna.lookiesapp.domain.model.painting.Painting

data class RegisterEventUiState(
    val isLoading: Boolean = false,
    val currentStep: Int = 0, // 0: Terms, 1: Select, 2: Review
    val eventId: Int = 0,
    val maxLimit: Int = 1,
    val fee: Double = 0.0,
    val merchantId: String = "",
    val allPaintings: List<Painting> = emptyList(),
    val selectedIds: Set<Int> = emptySet(),
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val data: RegisterEventOutput? = null,
    
    // Terms & Conditions data
    val revenueRules: List<EventRevenueRules> = emptyList(),
    val isRevenueLoading: Boolean = false,

    // Interactive Commission Proposal
    val proposedCommission: Float = 0.8f, // Default to 80% artist share
    val totalPaintingPrice: Double = 0.0
)
