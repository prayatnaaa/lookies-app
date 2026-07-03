package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.BusinessAddressDto
import com.prayatna.lookiesapp.domain.model.user.BusinessAddress

fun BusinessAddressDto.toDomain(): BusinessAddress {
    return BusinessAddress(
        country = country,
        city = city,
        streetLine1 = streetLine1,
        streetLine2 = streetLine2,
        district = district,
        subDistrict = subDistrict,
        provinceState = provinceState,
        postalCode = postalCode
    )
}

fun BusinessAddress.toDto(businessId: String): BusinessAddressDto {
    return BusinessAddressDto(
        businessId = businessId,
        country = country,
        city = city,
        streetLine1 = streetLine1,
        streetLine2 = streetLine2,
        district = district,
        subDistrict = subDistrict,
        provinceState = provinceState,
        postalCode = postalCode
    )
}
