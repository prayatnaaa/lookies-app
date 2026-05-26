package com.prayatna.lookiesapp.presentation.artistDashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.artist.GetArtistDashboardSummaryUseCase
import com.prayatna.lookiesapp.domain.usecase.merchant.GetMerchantProfileUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetMerchantBalanceLogsUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetPendingOrderSplitByMerchantIdUseCase
import com.prayatna.lookiesapp.presentation.artistDashboard.state.ArtistDashboardUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDashboardViewModel @Inject constructor(
    private val getDashboardSummaryUseCase: GetArtistDashboardSummaryUseCase,
    private val getMerchantBalanceLogsUseCase: GetMerchantBalanceLogsUseCase,
    private val getPendingOrderSplitByMerchantIdUseCase: GetPendingOrderSplitByMerchantIdUseCase,
    private val getMerchantProfileUseCase: GetMerchantProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ArtistDashboardUiState())
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            getDashboardSummaryUseCase()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error") }
                }
                .collectLatest { summary ->
                    _state.update { it.copy(summary = summary) }
                    
                    val businessId = summary.businessId
                    
                    // Fetch Merchant Profile to get Account ID
                    when (val profileResult = getMerchantProfileUseCase(businessId)) {
                        is DataResult.Success -> {
                            val profile = profileResult.data
                            val accountId = profile.accountId
                            
                            _state.update { it.copy(profile = profile) }
                            
                            // Fetch Balance Logs and Pending Splits
                            val pendingSplitsDeferred = async { getPendingOrderSplitByMerchantIdUseCase(accountId) }
                            val balanceLogsDeferred = async { getMerchantBalanceLogsUseCase(accountId) }
                            
                            val pendingResult = pendingSplitsDeferred.await()
                            if (pendingResult is DataResult.Success) {
                                _state.update { it.copy(pendingOrderSplits = pendingResult.data) }
                            }
                            
                            val balanceLogsResult = balanceLogsDeferred.await()
                            if (balanceLogsResult is DataResult.Success) {
                                _state.update { it.copy(balanceLogs = balanceLogsResult.data) }
                            }
                            
                            _state.update { it.copy(isLoading = false) }
                        }
                        is DataResult.Error -> {
                            _state.update { it.copy(isLoading = false, errorMessage = profileResult.error) }
                        }
                        else -> {
                            _state.update { it.copy(isLoading = false) }
                        }
                    }
                }
        }
    }
}
