package com.prayatna.lookiesapp.presentation.merchant.merchantMember

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.domain.usecase.merchant.GetMerchantMembersUseCase
import com.prayatna.lookiesapp.presentation.merchant.merchantMember.state.MerchantMemberListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantMemberListViewModel @Inject constructor(
    private val getMerchantMembersUseCase: GetMerchantMembersUseCase,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _uiState = MutableStateFlow(MerchantMemberListUiState())
    val uiState: StateFlow<MerchantMemberListUiState> = _uiState.asStateFlow()

    init {
        loadMerchantMembers()
    }

    fun loadMerchantMembers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val userId = userPreference.getProfile().first().id

            when (val result = getMerchantMembersUseCase(userId)) {
                is DataResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        merchantMembers = result.data,
                        errorMessage = null
                    )
                }
                is DataResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.error
                    )
                }
                else -> Unit
            }
        }
    }
}