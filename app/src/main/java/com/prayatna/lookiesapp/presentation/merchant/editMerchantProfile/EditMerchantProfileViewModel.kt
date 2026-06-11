package com.prayatna.lookiesapp.presentation.merchant.editMerchantProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.merchant.EditMerchantBankAccountInput
import com.prayatna.lookiesapp.domain.model.merchant.EditMerchantInput
import com.prayatna.lookiesapp.domain.usecase.merchant.EditMerchantBankAccountUseCase
import com.prayatna.lookiesapp.domain.usecase.merchant.EditMerchantBusinessUseCase
import com.prayatna.lookiesapp.domain.usecase.merchant.GetMerchantBankAccountsUseCase
import com.prayatna.lookiesapp.domain.usecase.merchant.GetMerchantProfileUseCase
import com.prayatna.lookiesapp.presentation.merchant.editMerchantProfile.state.EditMerchantProfileEvent
import com.prayatna.lookiesapp.presentation.merchant.editMerchantProfile.state.EditMerchantProfileState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMerchantProfileViewModel @Inject constructor(
    private val getMerchantProfileUseCase: GetMerchantProfileUseCase,
    private val getMerchantBankAccountsUseCase: GetMerchantBankAccountsUseCase,
    private val editMerchantBusinessUseCase: EditMerchantBusinessUseCase,
    private val editMerchantBankAccountUseCase: EditMerchantBankAccountUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditMerchantProfileState())
    val state = _state.asStateFlow()

    fun onEvent(event: EditMerchantProfileEvent) {
        when (event) {
            is EditMerchantProfileEvent.Load -> load(event.businessId)
            is EditMerchantProfileEvent.LegalNameChanged -> _state.update { it.copy(legalName = event.value) }
            is EditMerchantProfileEvent.TradingNameChanged -> _state.update { it.copy(tradingName = event.value) }
            is EditMerchantProfileEvent.DescriptionChanged -> _state.update { it.copy(description = event.value) }
            is EditMerchantProfileEvent.IndustryCategoryChanged -> _state.update { it.copy(industryCategory = event.value) }
            is EditMerchantProfileEvent.PhoneNumberChanged -> _state.update { it.copy(phoneNumber = event.value) }
            is EditMerchantProfileEvent.EmailChanged -> _state.update { it.copy(email = event.value) }
            is EditMerchantProfileEvent.WebsiteUrlChanged -> _state.update { it.copy(websiteUrl = event.value) }
            is EditMerchantProfileEvent.PictureUrlChanged -> _state.update { it.copy(pictureUrl = event.value) }
            
            is EditMerchantProfileEvent.BankSelected -> _state.update { it.copy(bankCode = event.code, bankName = event.name) }
            is EditMerchantProfileEvent.AccountNumberChanged -> _state.update { it.copy(accountNumber = event.value) }
            is EditMerchantProfileEvent.AccountHolderNameChanged -> _state.update { it.copy(accountHolderName = event.value) }
            
            EditMerchantProfileEvent.Save -> save()
            EditMerchantProfileEvent.DismissMessage -> _state.update { it.copy(errorMessage = null, successMessage = null) }
        }
    }

    private fun load(businessId: String) {
        // Prevent re-fetching if data is already present (e.g. returning from Select Bank)
        if (_state.value.profile != null && _state.value.profile?.businessId == businessId) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            val profileResult = getMerchantProfileUseCase(businessId)
            if (profileResult is DataResult.Success) {
                val profile = profileResult.data
                val bankResult = getMerchantBankAccountsUseCase(profile.accountId)
                
                val bankAccounts = if (bankResult is DataResult.Success) bankResult.data else emptyList()
                val primaryBank = bankAccounts.find { it.isPrimary } ?: bankAccounts.firstOrNull()
                
                _state.update { it.copy(
                    isLoading = false,
                    profile = profile,
                    bankAccounts = bankAccounts,
                    legalName = profile.legalName,
                    tradingName = profile.tradingName ?: "",
                    description = profile.description ?: "",
                    pictureUrl = profile.pictureUrl ?: "",
                    bankCode = primaryBank?.bankCode ?: "",
                    bankName = primaryBank?.bankName ?: "",
                    accountNumber = primaryBank?.accountNumber ?: "",
                    accountHolderName = primaryBank?.accountHolderName ?: "",
                    email =  profile.email ?: "",
                    phoneNumber = profile.phoneNumber ?: "",
                    websiteUrl = profile.websiteUrl ?: ""
                ) }
            } else if (profileResult is DataResult.Error) {
                _state.update { it.copy(isLoading = false, errorMessage = profileResult.error) }
            }
        }
    }

    private fun save() {
        val currentState = _state.value
        val businessId = currentState.profile?.businessId ?: return
        val bankAccountId = currentState.bankAccounts.find { it.isPrimary }?.id ?: currentState.bankAccounts.firstOrNull()?.id

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            
            coroutineScope {
                val businessDeferred = async {
                    editMerchantBusinessUseCase(
                        id = businessId,
                        input = EditMerchantInput(
                            legalName = currentState.legalName,
                            tradingName = currentState.tradingName,
                            description = currentState.description,
                            phoneNumber = currentState.phoneNumber.takeIf { it.isNotBlank() },
                            email = currentState.email.takeIf { it.isNotBlank() },
                            websiteUrl = currentState.websiteUrl.takeIf { it.isNotBlank() },
                            pictureUrl = currentState.pictureUrl.takeIf { it.isNotBlank() }
                        )
                    )
                }
                
                val bankDeferred = bankAccountId?.let { id ->
                    async {
                        editMerchantBankAccountUseCase(
                            id = id,
                            input = EditMerchantBankAccountInput(
                                bankCode = currentState.bankCode,
                                bankName = currentState.bankName,
                                accountNumber = currentState.accountNumber,
                                accountHolderName = currentState.accountHolderName
                            )
                        )
                    }
                }
                
                val businessResult = businessDeferred.await()
                val bankResult = bankDeferred?.await() ?: DataResult.Success(null)
                
                if (businessResult is DataResult.Success && bankResult is DataResult.Success) {
                    _state.update { it.copy(isSaving = false, successMessage = "Profile updated successfully") }
                } else {
                    val error = (businessResult as? DataResult.Error)?.error 
                        ?: (bankResult as? DataResult.Error)?.error 
                        ?: "Failed to update profile"
                    _state.update { it.copy(isSaving = false, errorMessage = error) }
                }
            }
        }
    }
}
