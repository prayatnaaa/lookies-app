package com.prayatna.lookiesapp.presentation.createPaintingReview.state

sealed class PaintingReviewEvent {
    data class ReviewMessageChanged(val value: String) : PaintingReviewEvent()
    data class StarRatingChanged(val value: Int) : PaintingReviewEvent()
    data object SubmitClicked : PaintingReviewEvent()
    data object BackClicked : PaintingReviewEvent()
}