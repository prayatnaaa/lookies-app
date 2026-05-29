package com.prayatna.lookiesapp.presentation.user.partnerSubmission.state

import android.net.Uri

data class PartnerSubmissionFormState(
    val merchantType: String = "partner",
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

    val useLoginEmail: Boolean = true,
    val businessEmail: String = "",

    val ownerFirstName: String = "",
    val ownerLastName: String = "",
    val ownerEmail: String = "",
    val ownerPhone: String = "",
    val ownerRole: String = "director",

    val selectedKycDocuments: List<Pair<String, Uri>> = emptyList(), // Pair of DocumentType to Uri

    val bankName: String = "",
    val bankCode: String = "",
    val accountNumber: String = "",
    val accountHolderName: String = "",
    val isPrimary: Boolean = true
)