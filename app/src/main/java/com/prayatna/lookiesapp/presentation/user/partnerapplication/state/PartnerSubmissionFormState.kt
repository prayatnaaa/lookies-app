package com.prayatna.lookiesapp.presentation.user.partnerapplication.state

import android.net.Uri

data class PartnerSubmissionFormState (
    val partnerName: String = "",
    val partnerType: String = "",
    val locName: String = "",
    val locUrl: String = "",
    val portfolioLink: String = "",
    val imageLogo: Uri? = null
)