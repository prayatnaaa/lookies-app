package com.prayatna.lookiesapp.presentation.partner.merchantMemberByMerchantId

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.merchant.GetMerchantMembersByMerchantIdUseCase
import com.prayatna.lookiesapp.presentation.partner.merchantMemberByMerchantId.state.MerchantMemberByMerchantIdEffect
import com.prayatna.lookiesapp.presentation.partner.merchantMemberByMerchantId.state.MerchantMemberByMerchantIdEvent
import com.prayatna.lookiesapp.presentation.partner.merchantMemberByMerchantId.state.MerchantMemberByMerchantIdUiState
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
class MerchantMemberByMerchantIdViewModel @Inject constructor(
    private val getMerchantMembersByMerchantIdUseCase: GetMerchantMembersByMerchantIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(MerchantMemberByMerchantIdUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MerchantMemberByMerchantIdEffect>()
    val effect = _effect.receiveAsFlow()

    private val businessId: String? = savedStateHandle["businessId"]

    init {
        businessId?.let { loadMembers(it) }
    }

    fun onEvent(event: MerchantMemberByMerchantIdEvent) {
        when (event) {
            MerchantMemberByMerchantIdEvent.BackClicked -> {
                viewModelScope.launch {
                    _effect.send(MerchantMemberByMerchantIdEffect.NavigateBack)
                }
            }
            MerchantMemberByMerchantIdEvent.Retry -> {
                businessId?.let { loadMembers(it) }
            }
        }
    }

    private fun loadMembers(businessId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            when (val result = getMerchantMembersByMerchantIdUseCase(businessId)) {
                is DataResult.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            merchantMembers = result.data,
                            errorMessage = null
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
                else -> Unit
            }
        }
    }
}
