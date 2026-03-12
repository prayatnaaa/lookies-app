package com.prayatna.lookiesapp.domain.usecase.payment

import com.prayatna.lookiesapp.domain.model.transaction.CardChannelProperties
import com.prayatna.lookiesapp.domain.model.transaction.CardDetails
import com.prayatna.lookiesapp.domain.model.transaction.CreatePaymentParams
import com.prayatna.lookiesapp.domain.model.transaction.CreateXenditPaymentRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.CreateXenditPaymentRequestResult
import com.prayatna.lookiesapp.domain.model.transaction.GopayChannelProperties
import com.prayatna.lookiesapp.domain.model.transaction.PaymentRequestMethod
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import java.util.UUID
import javax.inject.Inject

private fun String.normalizePhone(): String {
    return when {
        startsWith("0") -> "+62${substring(1)}"
        startsWith("+62") -> this
        else -> "+62$this"
    }
}

class CreateXenditPaymentUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(
        state: CreatePaymentParams,
        orderId: String,
        merchantId: String,
        amount: Double
    ): DataResult<CreateXenditPaymentRequestResult> {

        val referenceId = "TRX-${UUID.randomUUID()}"

        val request = when (state.selectedMethod) {

            PaymentRequestMethod.GOPAY -> {
                if (state.phoneNumber.isBlank()) {
                    return DataResult.Error("Nomor GoPay wajib diisi")
                }

                CreateXenditPaymentRequestInput(
                    merchantId = merchantId,
                    orderId = orderId,
                    referenceId = referenceId,
                    requestAmount = amount,
                    channelCode = "GOPAY_RECURRING",
                    channelProperties = GopayChannelProperties(
                        accountMobileNumber = state.phoneNumber.normalizePhone()
                    )
                )
            }

            PaymentRequestMethod.CREDIT_CARD -> {
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

                CreateXenditPaymentRequestInput(
                    merchantId = merchantId,
                    orderId = orderId,
                    referenceId = referenceId,
                    requestAmount = amount,
                    channelCode = "CARDS",
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
