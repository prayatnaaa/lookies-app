package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.DetailPartnerDto
import com.prayatna.lookiesapp.data.remote.dto.GetLocationDto
import com.prayatna.lookiesapp.data.remote.dto.PartnerData
import com.prayatna.lookiesapp.data.remote.dto.UserBankDto
import com.prayatna.lookiesapp.domain.model.partner.DetailPartner
import com.prayatna.lookiesapp.domain.model.partner.GetLocation
import com.prayatna.lookiesapp.domain.model.partner.UserBank

fun DetailPartnerDto.toDomain(): DetailPartner? {
    return if (success && data != null) {
        data.toDomain()
    } else {
        null
    }
}

fun PartnerData.toDomain(): DetailPartner {
    return DetailPartner(
        id = id,
        name = name,
        type = type,
        logoUrl = logoUrl,
        locations = locations.map { it.toDomain() },
        userBanks = userBanks.map { it.toDomain() },
        locationId = locationId,
        portofolioLink = portofolioLink,
        status = status,
        createdAt = createdAt,
        ktpOwnerUrl = ktpOwnerUrl,
        businessLicenseUrl = businessLicenseUrl,
        userId = userId
    )
}

private fun UserBankDto.toDomain(): UserBank {
    return UserBank(
        id = id,
        bankName = bankName,
        partnerId = partnerId,
        bankAccountHolder = bankAccountHolder,
        bankAccountNumber = bankAccountNumber,
    )
}

private fun GetLocationDto.toDomain(): GetLocation {
    return GetLocation(
        id = id,
        url = url,
        name = name,
        createdAt = createdAt,
        partnerId = partnerId
    )
}