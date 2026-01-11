package com.prayatna.lookiesapp.presentation.partner.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerHomeViewModel @Inject constructor(
    private val partnerRepository: PartnerRepository
): ViewModel() {
    private val _state = MutableStateFlow<PartnerHomeUiState>(PartnerHomeUiState.Loading)
    val state = _state.asStateFlow()

    init {
        getDashboardData()
    }

    fun getDashboardData() {
        viewModelScope.launch {
            _state.value = PartnerHomeUiState.Loading
            when (val result = partnerRepository.getDashboardSummary()) {
                is DataResult.Success -> {
                    _state.value = PartnerHomeUiState.Success(result.data)
                }
                is DataResult.Error -> {
                    _state.value = PartnerHomeUiState.Error(result.error)
                }
                else -> Unit
            }
        }
    }
}