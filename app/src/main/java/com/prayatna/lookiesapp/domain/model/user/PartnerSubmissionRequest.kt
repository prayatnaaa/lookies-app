package com.prayatna.lookiesapp.domain.model.user

import android.net.Uri

data class PartnerSubmissionRequest(
    val partnerName: String,
    val partnerType: String,
    val locationId: Int,
    val portfolioLink: String,
    val imageLogo: Uri
)
