package com.prayatna.lookiesapp.presentation.user.artistSubmission

sealed class ArtistSubmissionEffect {
    data class ShowSnackbar(val message: String) : ArtistSubmissionEffect()
    data object NavigateBack : ArtistSubmissionEffect()
    data object NavigateToSuccess : ArtistSubmissionEffect()
}