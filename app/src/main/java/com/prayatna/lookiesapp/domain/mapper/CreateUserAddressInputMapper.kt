package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.user.CreateUserAddressRequest
import com.prayatna.lookiesapp.domain.model.user.CreateUserAddressInput

fun CreateUserAddressInput.toDto() = CreateUserAddressRequest(
    userId = userId,
    name = name,
    addressLine = addressLine,
    province = province,
    phoneNumber = phoneNumber,
    postalCode = postalCode,
    city = city,
    isDefault = isDefault,
    notes = notes,
)