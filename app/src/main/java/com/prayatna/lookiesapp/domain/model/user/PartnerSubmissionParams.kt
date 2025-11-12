package com.prayatna.lookiesapp.domain.model.user

import android.net.Uri

data class PartnerSubmissionParams(
    val partnerName: String,
    val partnerType: String,
    val locName: String,
    val locUrl: String,
    val portfolioLink: String,
    val imageLogo: Uri
)
