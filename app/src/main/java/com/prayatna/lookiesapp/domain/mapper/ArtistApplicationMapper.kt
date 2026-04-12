package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.user.AddressDto
import com.prayatna.lookiesapp.data.remote.dto.request.user.ArtistApplicationRequest
import com.prayatna.lookiesapp.data.remote.dto.request.user.BankAccountDto
import com.prayatna.lookiesapp.data.remote.dto.request.user.GenderDto
import com.prayatna.lookiesapp.data.remote.dto.request.user.KYCDocumentDto
import com.prayatna.lookiesapp.domain.model.user.Address
import com.prayatna.lookiesapp.domain.model.user.ArtistApplicationInput
import com.prayatna.lookiesapp.domain.model.user.BankAccount
import com.prayatna.lookiesapp.domain.model.user.Gender
import com.prayatna.lookiesapp.domain.model.user.KYCDocument

fun ArtistApplicationRequest.toDomain(): ArtistApplicationInput {
    return ArtistApplicationInput(
        fullName = fullName,
        displayName = displayName,
        bio = bio,
        phoneNumber = phoneNumber,
        nationality = nationality,
        placeOfBirth = placeOfBirth,
        dateOfBirth = dateOfBirth,
        gender = gender?.toDomain(),
        website = website,
        country = country,
        address = address?.toDomain(),
        bankAccount = bankAccount.toDomain(),
        kycDocuments = kycDocuments?.map { it.toDomain() }
    )
}

fun ArtistApplicationInput.toDto(): ArtistApplicationRequest {
    return ArtistApplicationRequest(
        fullName = fullName,
        displayName = displayName,
        bio = bio,
        phoneNumber = phoneNumber,
        nationality = nationality,
        placeOfBirth = placeOfBirth,
        dateOfBirth = dateOfBirth,
        gender = gender?.toDto(),
        website = website,
        country = country,
        address = address?.toDto(),
        bankAccount = bankAccount.toDto(),
        kycDocuments = kycDocuments?.map { it.toDto() }
    )
}

fun AddressDto.toDomain() = Address(
    country, city, streetLine1, streetLine2,
    district, subDistrict, provinceState, postalCode
)

fun Address.toDto() = AddressDto(
    country, city, streetLine1, streetLine2,
    district, subDistrict, provinceState, postalCode
)

fun BankAccountDto.toDomain() = BankAccount(
    bankCode, bankName, accountNumber, accountHolderName
)

fun BankAccount.toDto() = BankAccountDto(
    bankCode, bankName, accountNumber, accountHolderName
)

fun KYCDocumentDto.toDomain() = KYCDocument(type, country, fileId)

fun KYCDocument.toDto() = KYCDocumentDto(type, country, fileId)

fun GenderDto.toDomain() = when (this) {
    GenderDto.MALE -> Gender.MALE
    GenderDto.FEMALE -> Gender.FEMALE
}

fun Gender.toDto() = when (this) {
    Gender.MALE -> GenderDto.MALE
    Gender.FEMALE -> GenderDto.FEMALE
}