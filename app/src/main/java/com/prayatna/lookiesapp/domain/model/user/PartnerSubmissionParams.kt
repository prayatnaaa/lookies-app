package com.prayatna.lookiesapp.domain.model.user

import android.net.Uri

data class PartnerSubmissionParams(
    val locName: String,
    val locUrl: String,
    val partnerName: String,
    val partnerType: String,
    val partnerLogo: Uri,
    val partnerPortfolioLink: String,
    val ktpFile: Uri,
    val businessLicenseFile: Uri,
    val bankName: String,
    val bankAccountNumber: String,
    val bankAccountHolder: String
)
