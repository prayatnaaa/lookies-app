package com.prayatna.lookiesapp.presentation.monthlyFinancleList.state

import com.prayatna.lookiesapp.domain.model.transaction.MerchantBalanceLog
import com.prayatna.lookiesapp.domain.model.transaction.OrderSplit

data class MonthlyFinanceUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val orderSplits: List<OrderSplit> = emptyList(),
    val balanceLogs: List<MerchantBalanceLog> = emptyList(),
    val selectedTab: Int = 0
)
