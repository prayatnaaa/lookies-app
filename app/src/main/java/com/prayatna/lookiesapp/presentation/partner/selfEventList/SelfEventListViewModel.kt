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
import kotlinx.coroutines.flow.update
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
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val currentStatus = _uiState.value.selectedStatus
            val currentQuery = _uiState.value.searchQuery.ifBlank { null }

            when (val response = partnerRepository.getSelfEvents(status = currentStatus, name = currentQuery)) {
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = response.error, isLoading = false)
                    }
                }
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(events = response.data, isLoading = false)
                    }
                }
                else -> Unit
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
    }

    fun onSearchTriggered() {
        loadSelfEvents()
    }

    fun onFilterSelected(status: String?) {
        val newStatus = if (_uiState.value.selectedStatus == status) null else status

        _uiState.update { it.copy(selectedStatus = newStatus) }
        loadSelfEvents()
    }

    fun retry() {
        loadSelfEvents()
    }
}