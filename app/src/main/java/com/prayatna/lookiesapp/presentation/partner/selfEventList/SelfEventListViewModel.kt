package com.prayatna.lookiesapp.presentation.partner.selfEventList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.presentation.partner.selfEventList.state.SelfEventListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelfEventListViewModel @Inject constructor(
    private val partnerRepository: PartnerRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SelfEventListUiState())
    val uiState: StateFlow<SelfEventListUiState> = _uiState.asStateFlow()

    init {
        loadSelfEvents()
    }

    private fun loadSelfEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val response = partnerRepository.getSelfEvents()) {
                is DataResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = response.error,
                        isLoading = false
                    )
                }

                is DataResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        events = response.data,
                        isLoading = false
                    )
                }
                else -> Unit
            }
        }
    }

    fun retry() {
        loadSelfEvents()
    }

}