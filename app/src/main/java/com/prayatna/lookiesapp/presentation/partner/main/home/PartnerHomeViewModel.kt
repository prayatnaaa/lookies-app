package com.prayatna.lookiesapp.presentation.partner.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReportFilterInput
import com.prayatna.lookiesapp.domain.usecase.merchant.GetMerchantProfileUseCase
import com.prayatna.lookiesapp.domain.usecase.partner.GetPartnerDashboardSummaryUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetMonthlyFinancialReportUseCase
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeEffect
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeEvent
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerHomeViewModel @Inject constructor(
    private val getMerchantProfileUseCase: GetMerchantProfileUseCase,
    private val getMonthlyFinancialReportUseCase: GetMonthlyFinancialReportUseCase,
    private val getPartnerDashboardSummaryUseCase: GetPartnerDashboardSummaryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PartnerHomeUiState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PartnerHomeEffect>()
    val effect = _effect.receiveAsFlow()

    private var businessId: String = ""

    fun onEvent(event: PartnerHomeEvent) {
        when (event) {

            is PartnerHomeEvent.Load -> {
                businessId = event.businessId
                loadData()
            }

            PartnerHomeEvent.Retry -> loadData()

            PartnerHomeEvent.BackClicked ->
                sendEffect(PartnerHomeEffect.NavigateBack)

            PartnerHomeEvent.CreateEventClicked ->
                sendEffect(PartnerHomeEffect.NavigateCreateEvent)

            PartnerHomeEvent.MyEventsClicked ->
                sendEffect(PartnerHomeEffect.NavigateMyEvents)

            PartnerHomeEvent.RefundClicked ->
                sendEffect(PartnerHomeEffect.NavigateRefund)

            PartnerHomeEvent.PaintingClicked ->
                sendEffect(PartnerHomeEffect.NavigatePainting)

            PartnerHomeEvent.ShipmentClicked ->
                sendEffect(PartnerHomeEffect.NavigateShipment)

            is PartnerHomeEvent.LoadMonthlyFinancialReport -> {
                loadMonthlyFinancialReport()
            }

            is PartnerHomeEvent.EndDateSelected -> {
                _state.update {
                    it.copy(filterEndDate = event.date)
                }
            }
            is PartnerHomeEvent.EventIdSelected -> {
                _state.update {
                    it.copy(filterEventId = event.id)
                }
            }
            is PartnerHomeEvent.ItemTypeSelected -> {
                _state.update {
                    it.copy(filterItemType = event.type)
                }
            }
            is PartnerHomeEvent.StartDateSelected -> {
                _state.update {
                    it.copy(filterStartDate = event.date)
                }
            }

            PartnerHomeEvent.MonthlyFinanceClicked -> {
                sendEffect(PartnerHomeEffect.NavigateMonthlyFinanceList)
            }

            PartnerHomeEvent.MemberListClicked -> {
                sendEffect(PartnerHomeEffect.NavigateMemberList(
                    merchantBusinessId = businessId
                ))
            }
        }
    }

    private fun loadMonthlyFinancialReport() {
        _state.update { it.copy(isLoading = true) }
        val filter = MonthlyFinancialReportFilterInput(
            merchantAccountId = businessId,
            startDate = _state.value.filterStartDate,
            endDate = _state.value.filterEndDate,
            itemType = _state.value.filterItemType,
            eventId = _state.value.filterEventId
        )
        viewModelScope.launch {
            when (val result = getMonthlyFinancialReportUseCase(filter)) {
                is DataResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.error
                    )
                    sendEffect(PartnerHomeEffect.ShowMessage(result.error))
                }
                is DataResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        monthlyFinancialReport = result.data
                    )
                }
                else -> Unit
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = getMerchantProfileUseCase(businessId)) {

                is DataResult.Success -> {

                    _state.value = _state.value.copy(
                        profile = result.data,
                        isLoading = true
                    )

                    getPartnerDashboardSummaryUseCase(businessId)
                        .catch { e ->
                            _state.value = _state.value.copy(
                                isLoading = false,
                                errorMessage = e.message
                            )
                        }
                        .collectLatest { dashboard ->

                            _state.value = _state.value.copy(
                                isLoading = false,
                                dashboard = dashboard
                            )
                        }
                }

                is DataResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.error
                    )
                }

                else -> Unit
            }
        }
    }

    private fun sendEffect(effect: PartnerHomeEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
