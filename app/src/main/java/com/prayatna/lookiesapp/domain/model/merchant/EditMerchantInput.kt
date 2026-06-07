package com.prayatna.lookiesapp.domain.model.merchant

data class EditMerchantInput(
    val legalName: String? = null,
    val tradingName: String? = null,
    val description: String? = null,
    val industryCategory: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val websiteUrl: String? = null,
    val pictureUrl: String? = null
)

data class EditMerchantBankAccountInput(
    val bankCode: String? = null,
    val bankName: String? = null,
    val accountNumber: String? = null,
    val accountHolderName: String? = null,
    val isPrimary: Boolean? = null
)
