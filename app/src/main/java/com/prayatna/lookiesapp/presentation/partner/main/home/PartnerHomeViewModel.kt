package com.prayatna.lookiesapp.presentation.partner.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.merchant.GetMerchantProfileUseCase
import com.prayatna.lookiesapp.domain.usecase.partner.GetPartnerDashboardSummaryUseCase
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerHomeViewModel @Inject constructor(
    private val getMerchantProfileUseCase: GetMerchantProfileUseCase,
    private val getPartnerDashboardSummaryUseCase: GetPartnerDashboardSummaryUseCase
): ViewModel() {
    private val _state = MutableStateFlow<PartnerHomeUiState>(PartnerHomeUiState.Loading)
    val state = _state.asStateFlow()

    fun getDashboardData(businessId: String) {
        viewModelScope.launch {
            _state.value = PartnerHomeUiState.Loading
            when (val result = getMerchantProfileUseCase(businessId)) {
                is DataResult.Success -> {
                    getPartnerDashboardSummaryUseCase()
                        .onStart { 
                            _state.value = PartnerHomeUiState.Success(profile = result.data)
                        }
                        .catch { e ->
                            _state.value = PartnerHomeUiState.Error(e.message ?: "Unknown error")
                        }
                        .collect { dashboard ->
                            _state.value = PartnerHomeUiState.Success(
                                profile = result.data,
                                dashboard = dashboard
                            )
                        }
                }
                is DataResult.Error -> {
                    _state.value = PartnerHomeUiState.Error(result.error)
                }
                else -> Unit
            }
        }
    }
}