package com.prayatna.lookiesapp.domain.usecase.payment

import com.prayatna.lookiesapp.data.remote.dto.request.payment.*
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateXenditPaymentResponse
import com.prayatna.lookiesapp.domain.model.transaction.CreatePaymentParams
import com.prayatna.lookiesapp.presentation.transaction.payment.state.PaymentMethod
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreateXenditPaymentUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(
        state: CreatePaymentParams,
        orderId: String,
        merchantId: String,
        amount: Double
    ): DataResult<CreateXenditPaymentResponse> {

        val referenceId = "TRX-${System.currentTimeMillis()}"

        val request = when (state.selectedMethod) {

            PaymentMethod.GOPAY -> {
                if (state.phoneNumber.isBlank()) {
                    return DataResult.Error("Nomor GoPay wajib diisi")
                }

                CreateXenditPaymentRequest(
                    merchantId = merchantId,
                    orderId = orderId,
                    referenceId = referenceId,
                    requestAmount = amount,
                    channelCode = PaymentMethod.GOPAY.code,
                    channelProperties = GopayChannelProperties(
                        accountMobileNumber = "+62" + state.phoneNumber
                    )
                )
            }

            PaymentMethod.CREDIT_CARD -> {
                if (
                    state.cardNumber.isBlank() ||
                    state.cardExpiry.isBlank() ||
                    state.cardCvv.isBlank()
                ) {
                    return DataResult.Error("Lengkapi data kartu")
                }

                val parts = state.cardExpiry.split("/")
                if (parts.size != 2) {
                    return DataResult.Error("Format expiry harus MM/YY")
                }

                val month = parts[0]
                val year = "20${parts[1]}"

                CreateXenditPaymentRequest(
                    merchantId = merchantId,
                    orderId = orderId,
                    referenceId = referenceId,
                    requestAmount = amount,
                    channelCode = PaymentMethod.CREDIT_CARD.code,
                    channelProperties = CardChannelProperties(
                        skipThreeDs = false,
                        cardDetails = CardDetails(
                            cardNumber = state.cardNumber,
                            expiryMonth = month,
                            expiryYear = year,
                            cardholderFirstName = "Customer",
                            cardholderLastName = "Name",
                            cardholderEmail = "customer@example.com"
                        )
                    )
                )
            }
        }

        return transactionRepository.createPaymentRequest(request)
    }
}
