package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.transaction.Shipment
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetShipmentByOrderIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(orderId: String): DataResult<Shipment> {
        return transactionRepository.getShipmentByOrderId(orderId)
    }

}