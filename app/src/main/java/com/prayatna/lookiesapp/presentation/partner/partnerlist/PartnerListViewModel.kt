package com.prayatna.lookiesapp.presentation.partner.partnerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness
import com.prayatna.lookiesapp.domain.usecase.partner.GetPartnersUseCase
import com.prayatna.lookiesapp.presentation.partner.partnerlist.state.MerchantBusinessType
import com.prayatna.lookiesapp.presentation.partner.partnerlist.state.PartnerListUiState
import com.prayatna.lookiesapp.presentation.partner.partnerlist.state.PartnerStatus
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerListViewModel @Inject constructor(
    private val getPartnersUseCase: GetPartnersUseCase,
    userPref: UserPreference,
): ViewModel() {

    private val _partnerState = MutableStateFlow<DataResult<List<MerchantBusiness>>>(DataResult.Idle)
    val partnerState: StateFlow<DataResult<List<MerchantBusiness>>> = _partnerState.asStateFlow()

    private val _uiState = MutableStateFlow(PartnerListUiState())
    val uiState: StateFlow<PartnerListUiState> = _uiState.asStateFlow()

    val roleState: StateFlow<String> = userPref.getRole()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    init {
        loadPartners()
    }

    fun loadPartners() {
        val type = _uiState.value.selectedType
        val status = _uiState.value.selectedStatus
        val title = _uiState.value.title.ifBlank { null }
        
        viewModelScope.launch {
            getPartnersUseCase(
                merchantType = type?.type,
                status = status?.value,
                name = title,
                kycStatus = null
            ).collect { result ->
                _partnerState.value = result
            }
        }
    }

    fun onTypeSelected(type: MerchantBusinessType?) {
        val newType = if (_uiState.value.selectedType == type) null else type
        _uiState.update { it.copy(selectedType = newType) }
        loadPartners()
    }

    fun onStatusSelected(status: PartnerStatus?) {
        val newStatus = if (_uiState.value.selectedStatus == status) null else status
        _uiState.update { it.copy(selectedStatus = newStatus) }
        loadPartners()
    }

    fun onTitleChanged(title: String) {
        _uiState.update { it.copy(title = title) }
    }
}
