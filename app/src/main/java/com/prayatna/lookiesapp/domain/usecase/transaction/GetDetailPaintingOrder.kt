package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.transaction.DetailPaintingOrder
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class GetDetailPaintingOrder @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(orderId: String): DataResult<DetailPaintingOrder> {
        return supervisorScope {
            val transaction = async { transactionRepository.getTransactionByOrderId(orderId) }
            val shipment = async { transactionRepository.getShipmentByOrderId(orderId) }

            val transactionResult = transaction.await()
            val shipmentResult = shipment.await()

            if (transactionResult is DataResult.Success && shipmentResult is DataResult.Success) {
                DataResult.Success(
                    DetailPaintingOrder(
                        transaction = transactionResult.data,
                        shipment = shipmentResult.data
                    ))
            } else {
                when {
                    transactionResult is DataResult.Error -> transactionResult
                    shipmentResult is DataResult.Error -> shipmentResult
                    else -> DataResult.Error("Something went wrong")
                }
            }
        }
    }
}