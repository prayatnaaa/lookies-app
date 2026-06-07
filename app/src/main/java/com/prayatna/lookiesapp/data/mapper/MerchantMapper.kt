package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.merchant.EditMerchantBankAccountRequest
import com.prayatna.lookiesapp.data.remote.dto.request.merchant.EditMerchantBusinessRequest
import com.prayatna.lookiesapp.domain.model.merchant.EditMerchantBankAccountInput
import com.prayatna.lookiesapp.domain.model.merchant.EditMerchantInput

fun EditMerchantInput.toDto(): EditMerchantBusinessRequest {
    return EditMerchantBusinessRequest(
        legalName = legalName,
        tradingName = tradingName,
        description = description,
        industryCategory = industryCategory,
        phoneNumber = phoneNumber,
        email = email,
        websiteUrl = websiteUrl,
        pictureUrl = pictureUrl
    )
}

fun EditMerchantBankAccountInput.toDto(): EditMerchantBankAccountRequest {
    return EditMerchantBankAccountRequest(
        bankCode = bankCode,
        bankName = bankName,
        accountNumber = accountNumber,
        accountHolderName = accountHolderName,
        isPrimary = isPrimary
    )
}
