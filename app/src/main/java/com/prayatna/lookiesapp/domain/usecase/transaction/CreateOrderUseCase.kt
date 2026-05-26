package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        items: List<OrderItemInput>,
        shippingCost: Double,
        recipientName: String,
        phoneNumber: String,
        addressLine: String,
        province: String,
        postalCode: String
    ) = transactionRepository.createOrder(
        items,
        shippingCost,
        recipientName,
        phoneNumber,
        addressLine,
        province,
        postalCode
    )
}