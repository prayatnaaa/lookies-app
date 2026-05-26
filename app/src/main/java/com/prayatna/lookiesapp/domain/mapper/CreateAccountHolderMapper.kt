package com.prayatna.lookiesapp.domain.mapper


import com.prayatna.lookiesapp.domain.model.user.*
import com.prayatna.lookiesapp.data.remote.dto.request.user.*

fun RoleApplicationInput.toDto(): RoleApplicationRequest {
    return RoleApplicationRequest(
        useLoginEmail = useLoginEmail,
        businessEmail = businessEmail,
        businessPayload = businessPayload.toDto(),
        merchantType = merchantType,
        bankAccounts = bankAccounts.toDto()
    )
}

fun CreateAccountHolderInput.toDto(): CreateAccountHolderRequest =
    CreateAccountHolderRequest(
        businessDetail = businessDetail.toDto(),
        address = address.toDto(),
        individualDetails = individualDetails.map { it.toDto() },
        kycDocuments = kycDocuments.map { it.toDto() },
        websiteUrl = websiteUrl,
        phoneNumber = phoneNumber,
        email = email,
        userId = userId
    )

fun BankAccount.toDto(): BankAccountDto {
    return BankAccountDto(
        bankCode = bankCode,
        bankName = bankName,
        accountNumber = accountNumber,
        accountHolderName
    )
}

fun BusinessDetail.toDto(): BusinessDetailDto =
    BusinessDetailDto(
        type = type,
        legalName = legalName,
        tradingName = tradingName,
        description = description,
        industryCategory = industryCategory,
        dateOfRegistration = dateOfRegistration,
        countryOfOperation = countryOfOperation
    )

fun BusinessAddress.toDto(): BusinessAddressDto =
    BusinessAddressDto(
        country = country,
        city = city,
        streetLine1 = streetLine1,
        streetLine2 = streetLine2,
        district = district,
        subDistrict = subDistrict,
        provinceState = provinceState,
        postalCode = postalCode
    )

fun IndividualDetail.toDto(): IndividualDetailDto =
    IndividualDetailDto(
        givenNames = givenNames,
        surname = surname,
        phoneNumber = phoneNumber,
        email = email,
        nationality = nationality,
        placeOfBirth = placeOfBirth,
        dateOfBirth = dateOfBirth,
        gender = gender,
        type = type,
        role = role
    )

fun KycDocument.toDto(): KycDocumentDto =
    KycDocumentDto(
        type = type,
        country = country,
        fileId = fileId
    )
