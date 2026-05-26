package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.UserAddressDto
import com.prayatna.lookiesapp.domain.model.user.UserAddress

fun UserAddressDto.toDomain() = UserAddress(
    id = id,
    userId = userId,
    name = name,
    addressLine = addressLine,
    province = province,
    phoneNumber = phoneNumber,
    postalCode = postalCode,
    city = city,
    isDefault = isDefault,
    notes = notes,
    createdAt = createdAt
)