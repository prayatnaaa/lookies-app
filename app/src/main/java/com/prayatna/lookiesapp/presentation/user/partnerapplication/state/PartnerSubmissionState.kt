package com.prayatna.lookiesapp.presentation.user.partnerapplication.state

data class PartnerSubmissionState (
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)