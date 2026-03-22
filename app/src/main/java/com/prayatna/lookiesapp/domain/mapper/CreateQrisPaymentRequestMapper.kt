package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateQrisPaymentRequestRequest
import com.prayatna.lookiesapp.data.remote.dto.response.payment.ChannelPropertiesDto
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateQrisPaymentRequestResponse
import com.prayatna.lookiesapp.data.remote.dto.response.payment.PaymentDataDto
import com.prayatna.lookiesapp.data.remote.dto.response.payment.PaymentMethodDto
import com.prayatna.lookiesapp.data.remote.dto.response.payment.QrCodeDto
import com.prayatna.lookiesapp.domain.model.transaction.CreateQrisPaymentRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.CreateQrisPaymentRequestResult
import com.prayatna.lookiesapp.domain.model.transaction.QrCode
import com.prayatna.lookiesapp.domain.model.transaction.QrisChannelProperties
import com.prayatna.lookiesapp.domain.model.transaction.QrisPaymentData
import com.prayatna.lookiesapp.domain.model.transaction.QrisPaymentMethod

fun CreateQrisPaymentRequestInput.toDto() = CreateQrisPaymentRequestRequest(
    merchantId = merchantId,
    orderId = orderId,
    amount = amount,
    description = description
)

fun CreateQrisPaymentRequestResponse.toDomain() = CreateQrisPaymentRequestResult(
    status = status,
    message = message,
    data = data.toDomain()
)

fun PaymentDataDto.toDomain() = QrisPaymentData(
    id = id,
    country = country,
    amount = amount,
    currency = currency,
    businessId = businessId,
    referenceId = referenceId,
    paymentMethod = paymentMethod.toDomain(),
    description = description,
    status = status,
    created = created,
    updated = updated
)

fun PaymentMethodDto.toDomain() = QrisPaymentMethod(
    id = id,
    type = type,
    referenceId = referenceId,
    status = status,
    qrCode = qrCode?.toDomain()
)

fun QrCodeDto.toDomain() = QrCode(
    amount = amount,
    currency = currency,
    channelCode = channelCode,
    channelProperties = channelProperties.toDomain()
)

fun ChannelPropertiesDto.toDomain() = QrisChannelProperties(
    qrString = qrString
)