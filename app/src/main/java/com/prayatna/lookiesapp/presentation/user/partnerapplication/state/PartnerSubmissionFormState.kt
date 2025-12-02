package com.prayatna.lookiesapp.presentation.user.partnerapplication.state

import android.net.Uri

data class PartnerSubmissionFormState(
    val locName: String = "",
    val locUrl: String = "",
    val partnerName: String = "",
    val partnerType: String = "",
    val partnerLogo: Uri? = null,
    val partnerPortfolioLink: String = "",
    val ktpFile: Uri? = null,
    val businessLicenseFile: Uri? = null,
    val bankName: String = "",
    val bankAccountNumber: String = "",
    val bankAccountHolder: String = ""
)
