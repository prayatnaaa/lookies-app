package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.data.remote.dto.response.payment.SetOrderToCompleteInput
import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.model.payment.PaymentAttempt
import com.prayatna.lookiesapp.domain.model.payment.SetOrderToCompleteResult
import com.prayatna.lookiesapp.domain.model.ticket.Ticket
import com.prayatna.lookiesapp.domain.model.transaction.CreateQrisPaymentRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.CreateQrisPaymentRequestResult
import com.prayatna.lookiesapp.domain.model.transaction.CreateXenditPaymentRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.CreateXenditPaymentRequestResult
import com.prayatna.lookiesapp.domain.model.transaction.MerchantBalanceLog
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReport
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReportFilterInput
import com.prayatna.lookiesapp.domain.model.transaction.OrderSplit
import com.prayatna.lookiesapp.domain.model.transaction.PayoutResult
import com.prayatna.lookiesapp.domain.model.transaction.PendingOrderSplits
import com.prayatna.lookiesapp.domain.model.transaction.Transaction
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun getPendingOrderSplitByMerchantId(merchantId: String):
            DataResult<PendingOrderSplits>
    suspend fun getOrderSplitsByMerchantId(merchantId: String):
            DataResult<List<OrderSplit>>
    suspend fun getMerchantBalanceLogs(merchantId: String):
            DataResult<List<MerchantBalanceLog>>
    suspend fun createOrder(
        items: List<OrderItemInput>,
        shippingCost: Double,
        recipientName: String,
        phoneNumber: String,
        addressLine: String,
        province: String,
        postalCode: String
    ):
            DataResult<String>
    suspend fun createQrisPaymentRequest(data: CreateQrisPaymentRequestInput):
            DataResult<CreateQrisPaymentRequestResult>
    suspend fun getUserTransactions():
            DataResult<List<Transaction>>
    suspend fun createPaymentRequest(request: CreateXenditPaymentRequestInput):
            DataResult<CreateXenditPaymentRequestResult>
    fun getPaymentAttempt(orderId: String):
            Flow<DataResult<PaymentAttempt>>
    suspend fun getTicketsByOrderId(orderId: String):
            DataResult<List<Ticket>>
    suspend fun getTransactionByOrderId(orderId: String):
            DataResult<Transaction>
    suspend fun getOrderDetail(orderId: String):
            DataResult<Transaction>
    suspend fun setOrderToComplete(request: SetOrderToCompleteInput):
            DataResult<SetOrderToCompleteResult>
    suspend fun getMonthlyFinancialReport(filter: MonthlyFinancialReportFilterInput):
            DataResult<List<MonthlyFinancialReport>>
    suspend fun payouts(withdrawalId: String):
            DataResult<PayoutResult>
}