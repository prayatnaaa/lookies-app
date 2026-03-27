package com.prayatna.lookiesapp.presentation.partner.partnerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness
import com.prayatna.lookiesapp.domain.usecase.partner.GetPartnersUseCase
import com.prayatna.lookiesapp.presentation.partner.partnerlist.state.MerchantBusinessType
import com.prayatna.lookiesapp.presentation.partner.partnerlist.state.PartnerListUiState
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
//    private val approvePartnerUseCase: ApprovePartnerUseCase,
//    private val rejectPartnerUseCase: RejectPartnerUseCase
): ViewModel() {

    private val _partnerState = MutableStateFlow<DataResult<List<MerchantBusiness>>>(DataResult.Idle)
    val partnerState: StateFlow<DataResult<List<MerchantBusiness>>> = _partnerState.asStateFlow()

    private val _uiState = MutableStateFlow(PartnerListUiState())
    val uiState: StateFlow<PartnerListUiState> = _uiState.asStateFlow()

    val roleState: StateFlow<String> = userPref.getRole()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

//    private val _actionMessage = MutableSharedFlow<String>()

    init {
        loadPartners()
    }

    fun loadPartners() {
        val merchantType = _uiState.value.selectedStatus
        viewModelScope.launch {
            getPartnersUseCase(
                merchantType = merchantType?.type,
                status = null,
                name = null,
                kycStatus = null
            ).collect { result ->
                _partnerState.value = result
            }
        }
    }

    fun onFilterSelected(status: MerchantBusinessType?) {
        val newStatus = if (_uiState.value.selectedStatus == status) null else status
        _uiState.update { it.copy(selectedStatus = newStatus) }
        loadPartners()
    }

//    fun approvePartner(partnerId: String) {
//        decidePartner(partnerId, approvePartnerUseCase::invoke)
//    }
//
//    fun rejectPartner(partnerId: String) {
//        decidePartner(partnerId, rejectPartnerUseCase::invoke)
//    }

//    private fun decidePartner(
//        partnerId: String,
//        action: suspend (String) -> DataResult<String>
//    ) {
//        viewModelScope.launch {
//            when (val result = action(partnerId)) {
//                is DataResult.Success -> {
//                    _actionMessage.emit(result.data)
//                    loadPartners()
//                }
//                is DataResult.Error -> {
//                    _actionMessage.emit(result.error)
//                }
//                else -> Unit
//            }
//        }
//    }
}