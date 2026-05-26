package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateXenditPaymentRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.ChannelPropertiesDto
import com.prayatna.lookiesapp.data.remote.dto.response.payment.ActionDto
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateXenditPaymentResponse
import com.prayatna.lookiesapp.data.remote.dto.response.payment.TChannelPropertiesDto
import com.prayatna.lookiesapp.data.remote.dto.response.payment.XenditPaymentTokenDto
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CardChannelProperties as CardChannelPropertiesDto
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CardDetails as CardDetailsDto
import com.prayatna.lookiesapp.data.remote.dto.request.payment.GopayChannelProperties as GopayChannelPropertiesDto

import com.prayatna.lookiesapp.domain.model.transaction.*

fun CreateXenditPaymentRequestInput.toDto(): CreateXenditPaymentRequest {
    return CreateXenditPaymentRequest(
        merchantId = merchantId,
        orderId = orderId,
        referenceId = referenceId,
        type = type,
        country = country,
        currency = currency,
        channelCode = channelCode,
        channelProperties = channelProperties.toDto(),
        requestAmount = requestAmount,
        captureMethod = captureMethod,
        customerId = customerId
    )
}

fun ChannelProperties.toDto(): ChannelPropertiesDto {
    return when (this) {

        is CardChannelProperties -> CardChannelPropertiesDto(
            midLabel = midLabel,
            skipThreeDs = skipThreeDs,
            cardOnFileType = cardOnFileType,
            failureReturnUrl = failureReturnUrl,
            successReturnUrl = successReturnUrl,
            statementDescriptor = statementDescriptor,
            cardDetails = CardDetailsDto(
                cardNumber = cardDetails.cardNumber,
                expiryYear = cardDetails.expiryYear,
                expiryMonth = cardDetails.expiryMonth,
                cardholderFirstName = cardDetails.cardholderFirstName,
                cardholderLastName = cardDetails.cardholderLastName,
                cardholderEmail = cardDetails.cardholderEmail
            )
        )

        is GopayChannelProperties -> GopayChannelPropertiesDto(
            accountMobileNumber = accountMobileNumber
        )
    }
}

fun CreateXenditPaymentResponse.toDomain(): CreateXenditPaymentRequestResult {
    return  CreateXenditPaymentRequestResult(
        status = status,
        message = message,
        paymentAttemptId = paymentAttemptId,
        paymentToken = paymentToken?.toDomain(),
    )
}

fun XenditPaymentTokenDto.toDomain(): XenditPaymentToken {
    return XenditPaymentToken(
        paymentRequestId = paymentRequestId,
        country = country,
        currency = currency,
        businessId = businessId,
        referenceId = referenceId,
        created = created,
        updated = updated,
        status = status,
        captureMethod = captureMethod,
        channelCode = channelCode,
        customerId = customerId,
        requestAmount = requestAmount,
        channelProperties = channelProperties.toDomain(),
        type = type,
        actions = actions.map { it.toDomain() }
    )
}

fun TChannelPropertiesDto.toDomain(): TChannelProperties {
    return TChannelProperties(
        successReturnUrl = successReturnUrl,
        failureReturnUrl = failureReturnUrl,
        cancelReturnUrl = cancelReturnUrl,
        accountMobileNumber = accountMobileNumber,
    )
}

fun ActionDto.toDomain(): Action {
    return Action(
        type = type,
        descriptor = descriptor,
        value = value,
    )
}