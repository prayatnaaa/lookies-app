package com.prayatna.lookiesapp.presentation.merchant.editMerchantProfile.state

import com.prayatna.lookiesapp.domain.model.merchant.MerchantBankAccount
import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile

data class EditMerchantProfileState(
    val isLoading: Boolean = false,
    val profile: MerchantProfile? = null,
    val bankAccounts: List<MerchantBankAccount> = emptyList(),
    
    // Business Form
    val legalName: String = "",
    val tradingName: String = "",
    val description: String = "",
    val industryCategory: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val websiteUrl: String = "",
    val pictureUrl: String = "",
    
    // Bank Form (Primary Account)
    val bankCode: String = "",
    val bankName: String = "",
    val accountNumber: String = "",
    val accountHolderName: String = "",
    
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
