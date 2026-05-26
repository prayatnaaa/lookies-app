package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.MemberDto
import com.prayatna.lookiesapp.data.remote.dto.MerchantAddressDto
import com.prayatna.lookiesapp.data.remote.dto.MerchantBankAccountDto
import com.prayatna.lookiesapp.data.remote.dto.MerchantDetailDto
import com.prayatna.lookiesapp.data.remote.dto.MerchantIndividualDto
import com.prayatna.lookiesapp.domain.model.merchant.Member
import com.prayatna.lookiesapp.domain.model.merchant.MerchantAddress
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBankAccount
import com.prayatna.lookiesapp.domain.model.merchant.MerchantIndividual

fun MerchantDetailDto.toDomain(): com.prayatna.lookiesapp.domain.model.merchant.MerchantDetail {
    return com.prayatna.lookiesapp.domain.model.merchant.MerchantDetail(
        id = id,
        type = type,
        legalName = legalName,
        tradingName = tradingName,
        description = description,
        industryCategory = industryCategory,
        dateOfRegistration = dateOfRegistration,
        countryOfOperation = countryOfOperation,
        websiteUrl = websiteUrl,
        phoneNumber = phoneNumber,
        email = email,
        pictureUrl = pictureUrl,
        merchantType = merchantType,
        createdAt = createdAt,
        updatedAt = updatedAt,
        merchantAccountId = merchantAccountId,
        kycStatus = kycStatus,
        kycRejectionReason = kycRejectionReason,
        payoutEnabled = payoutEnabled,
        ownerUserId = ownerUserId,
        ownerEmail = ownerEmail,
        ownerName = ownerName,
        ownerPicture = ownerPicture,
        addresses = addresses.map { it.toDomain() },
        bankAccounts = bankAccounts.map { it.toDomain() },
        members = members.map { it.toDomain() },
        individuals = individuals.map { it.toDomain() }
    )
}

fun MerchantAddressDto.toDomain(): MerchantAddress {
    return MerchantAddress(
        id = id,
        country = country,
        province = province,
        city = city,
        district = district,
        subDistrict = subDistrict,
        streetLine1 = streetLine1,
        streetLine2 = streetLine2,
        postalCode = postalCode
    )
}

fun MerchantBankAccountDto.toDomain(): MerchantBankAccount {
    return MerchantBankAccount(
        id = id,
        bankCode = bankCode,
        bankName = bankName,
        accountNumber = accountNumber,
        accountHolderName = accountHolderName,
        isPrimary = isPrimary
    )
}

fun MemberDto.toDomain(): Member {
    return Member(
        userId = userId,
        role = role,
        status = status,
        name = name,
        email = email
    )
}

fun MerchantIndividualDto.toDomain(): MerchantIndividual {
    return MerchantIndividual(
        id = id,
        givenNames = givenNames,
        surname = surname,
        email = email,
        phoneNumber = phoneNumber,
        role = role
    )
}