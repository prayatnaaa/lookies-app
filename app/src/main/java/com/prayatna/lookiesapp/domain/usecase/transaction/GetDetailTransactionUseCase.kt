package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.transaction.DetailTransaction
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class GetDetailTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(orderId: String): DataResult<DetailTransaction> {
        return supervisorScope {
            val transactionDef = async { transactionRepository.getTransactionByOrderId(orderId) }
            val ticketsDef = async { transactionRepository.getTicketsByOrderId(orderId) }

            val transaction = transactionDef.await()
            val tickets = ticketsDef.await()

            if (transaction is DataResult.Success && tickets is DataResult.Success) {
                DataResult.Success(
                    DetailTransaction(
                        transaction = transaction.data,
                        tickets = tickets.data
                    )
                )
            } else {
                when {
                    transaction is DataResult.Error -> transaction
                    tickets is DataResult.Error -> tickets
                    else -> DataResult.Error("Something went wrong")
                }
            }
        }
    }
}