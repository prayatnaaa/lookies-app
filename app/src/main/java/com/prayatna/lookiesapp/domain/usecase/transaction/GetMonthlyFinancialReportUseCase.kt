package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReport
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReportFilterInput
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetMonthlyFinancialReportUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(filter: MonthlyFinancialReportFilterInput): DataResult<List<MonthlyFinancialReport>> {
        return repository.getMonthlyFinancialReport(filter)
    }
}