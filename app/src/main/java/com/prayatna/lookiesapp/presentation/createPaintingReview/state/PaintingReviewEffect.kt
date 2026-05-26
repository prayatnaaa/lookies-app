package com.prayatna.lookiesapp.presentation.createPaintingReview.state

sealed class PaintingReviewEffect {
    data class ShowBottomDropdown(val status: String, val message: String) : PaintingReviewEffect()
    data object NavigateBack : PaintingReviewEffect()
}