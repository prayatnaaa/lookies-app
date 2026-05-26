package com.prayatna.lookiesapp.presentation.user.artistSubmission.state

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.user.Gender

data class ArtistSubmissionFormState(
    val fullName: String = "",
    val displayName: String = "",
    val bio: String = "",
    val phoneNumber: String = "",
    val nationality: String = "",
    val placeOfBirth: String = "",
    val dateOfBirth: String = "",
    val gender: Gender? = null,
    val website: String = "",
    val country: String = "",
    
    // Address
    val streetLine1: String = "",
    val streetLine2: String = "",
    val city: String = "",
    val province: String = "",
    val district: String = "",
    val subDistrict: String = "",
    val postalCode: String = "",
    
    // Bank Account
    val bankCode: String = "",
    val bankName: String = "",
    val accountNumber: String = "",
    val accountHolderName: String = "",
    
    // KYC Document
    val kycFileName: String = "No file chosen",
    val kycFileBytes: Uri? = null,
    val kycFileType: String = "ID_CARD"
)
