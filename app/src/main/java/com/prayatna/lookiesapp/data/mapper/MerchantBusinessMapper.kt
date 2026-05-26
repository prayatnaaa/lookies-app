package com.prayatna.lookiesapp.data.mapper


import com.prayatna.lookiesapp.data.remote.dto.MerchantBusinessDto
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness

fun MerchantBusinessDto.toDomain(): MerchantBusiness {
    return MerchantBusiness(
        id = this.id,
        type = this.type,
        legalName = this.legalName,
        tradingName = this.tradingName,
        description = this.description,
        industryCategory = this.industryCategory,
        dateOfRegistration = this.dateOfRegistration,
        countryOfOperation = this.countryOfOperation,
        websiteUrl = this.websiteUrl,
        phoneNumber = this.phoneNumber,
        email = this.email,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        pictureUrl = this.pictureUrl,
        merchantType = this.merchantType,
        status = this.status
    )
}

fun MerchantBusiness.toDto(): MerchantBusinessDto {
    return MerchantBusinessDto(
        id = this.id,
        type = this.type,
        legalName = this.legalName,
        tradingName = this.tradingName,
        description = this.description,
        industryCategory = this.industryCategory,
        dateOfRegistration = this.dateOfRegistration,
        countryOfOperation = this.countryOfOperation,
        websiteUrl = this.websiteUrl,
        phoneNumber = this.phoneNumber,
        email = this.email,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        pictureUrl = this.pictureUrl,
        merchantType = this.merchantType,
        status = this.status
    )
}