package com.prayatna.lookiesapp.presentation.partner.partnerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.domain.model.user.MerchantBusiness
import com.prayatna.lookiesapp.domain.usecase.partner.GetPartnersUseCase
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerListViewModel @Inject constructor(
    private val getPartnersUseCase: GetPartnersUseCase,
    private val userPref: UserPreference
): ViewModel() {

    private val _partnerState = MutableStateFlow<DataResult<List<MerchantBusiness>>>(DataResult.Idle)
    val partnerState: StateFlow<DataResult<List<MerchantBusiness>>> = _partnerState

    val roleState: StateFlow<String> = userPref.getRole()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    init {
        loadPartners()
    }

    fun loadPartners() {
        viewModelScope.launch {
            getPartnersUseCase()
                .collect { result ->
                    _partnerState.value = result
                }
        }
    }
}