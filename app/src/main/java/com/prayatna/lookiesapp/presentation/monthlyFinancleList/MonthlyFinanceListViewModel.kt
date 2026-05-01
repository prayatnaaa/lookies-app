package com.prayatna.lookiesapp.presentation.monthlyFinancleList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReportFilterInput
import com.prayatna.lookiesapp.domain.usecase.transaction.GetMonthlyFinancialReportUseCase
import com.prayatna.lookiesapp.presentation.monthlyFinancleList.state.MonthlyFinanceEffect
import com.prayatna.lookiesapp.presentation.monthlyFinancleList.state.MonthlyFinanceEvent
import com.prayatna.lookiesapp.presentation.monthlyFinancleList.state.MonthlyFinanceUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthlyFinanceListViewModel @Inject constructor(
    private val getMonthlyFinancialReportUseCase: GetMonthlyFinancialReportUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val businessId = checkNotNull(savedStateHandle.get<String>("businessId"))

    private val _state = MutableStateFlow(MonthlyFinanceUiState())
    val state = _state.asStateFlow()

    private val _effect = Channel<MonthlyFinanceEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: MonthlyFinanceEvent) {
        when (event) {
            is MonthlyFinanceEvent.EndDateSelected -> {
                _state.update { it.copy( filterEndDate = event.date) }
            }
            is MonthlyFinanceEvent.EventIdSelected -> {
                _state.update { it.copy( filterEventId = event.id) }
            }
            is MonthlyFinanceEvent.GetMonthlyFinancialReport -> {
                loadMonthlyFinancialReport()
            }
            is MonthlyFinanceEvent.ItemTypeSelected -> {
                _state.update { it.copy( filterItemType = event.type) }
            }
            MonthlyFinanceEvent.NavigateBack -> {
                viewModelScope.launch {
                    _effect.send(MonthlyFinanceEffect.NavigateBack)
                }
            }
            is MonthlyFinanceEvent.StartDateSelected -> {
                _state.update { it.copy( filterStartDate = event.date) }
            }
        }
    }

    private fun loadMonthlyFinancialReport() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val filter = MonthlyFinancialReportFilterInput(
                startDate = _state.value.filterStartDate,
                endDate = _state.value.filterEndDate,
                itemType = _state.value.filterItemType,
                eventId = _state.value.filterEventId,
                merchantAccountId = businessId
            )

            when (val result = getMonthlyFinancialReportUseCase(filter)) {
                is DataResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                    _effect.send(MonthlyFinanceEffect.ShowToast(title = "Error", message = result.error))
                }
                is DataResult.Success-> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            monthlyFinancialReports = result.data
                        )
                    }
                }
                else -> Unit
            }
        }
    }
}