package com.prayatna.lookiesapp.presentation.user.partnerSubmission.state

import android.net.Uri

data class PartnerSubmissionFormState(
    val legalName: String = "",
    val tradingName: String = "",
    val description: String = "",
    val businessType: String = "CORPORATION",
    val industryCategory: String = "ART_AND_CRAFTS_NEW",
    val countryOperation: String = "ID",

    val streetLine1: String = "",
    val city: String = "",
    val province: String = "",
    val postalCode: String = "",

    val ownerFirstName: String = "",
    val ownerLastName: String = "",
    val ownerEmail: String = "",
    val ownerPhone: String = "",
    val ownerRole: String = "director",

    val kycFileName: String = "ktp.png",
    val kycFileBytes: Uri? = null,
    val kycFileType: String = "AUTHORIZED_PERSON_KTP_DOCUMENT"
)