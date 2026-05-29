package com.prayatna.lookiesapp.presentation.user.partnerSubmission

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.user.BankAccount
import com.prayatna.lookiesapp.domain.model.user.BusinessAddress
import com.prayatna.lookiesapp.domain.model.user.BusinessDetail
import com.prayatna.lookiesapp.domain.model.user.CreateAccountHolderInput
import com.prayatna.lookiesapp.domain.model.user.IndividualDetail
import com.prayatna.lookiesapp.domain.model.user.KycDocument
import com.prayatna.lookiesapp.domain.model.user.RoleApplicationInput
import com.prayatna.lookiesapp.domain.usecase.payment.GetPayoutChannelsUseCase
import com.prayatna.lookiesapp.domain.usecase.user.RegisterBusinessUseCase
import com.prayatna.lookiesapp.presentation.user.partnerSubmission.state.PartnerSubmissionEvent
import com.prayatna.lookiesapp.presentation.user.partnerSubmission.state.PartnerSubmissionFormState
import com.prayatna.lookiesapp.presentation.user.partnerSubmission.state.PartnerSubmissionUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerSubmissionViewModel @Inject constructor(
    private val registerBusinessUseCase: RegisterBusinessUseCase,
    private val getPayoutChannelsUseCase: GetPayoutChannelsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val merchantType: String =
        savedStateHandle["merchantType"] ?: ""

    private val _uiState = MutableStateFlow<PartnerSubmissionUiState>(PartnerSubmissionUiState.Idle)
    val uiState: StateFlow<PartnerSubmissionUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(PartnerSubmissionFormState())
    val formState: StateFlow<PartnerSubmissionFormState> = _formState.asStateFlow()

    init {
        loadPayoutChannels()
    }

    private fun loadPayoutChannels() {
        viewModelScope.launch {
            when (val result = getPayoutChannelsUseCase()) {
                is DataResult.Success -> {
                    _uiState.value = PartnerSubmissionUiState.MetaLoaded(result.data)
                }
                else -> Unit
            }
        }
    }

    fun onEvent(event: PartnerSubmissionEvent) {
        when (event) {
            is PartnerSubmissionEvent.LegalNameChanged -> _formState.update { it.copy(legalName = event.value) }
            is PartnerSubmissionEvent.TradingNameChanged -> _formState.update { it.copy(tradingName = event.value) }
            is PartnerSubmissionEvent.DescriptionChanged -> _formState.update { it.copy(description = event.value) }
            is PartnerSubmissionEvent.AddressChanged -> _formState.update { it.copy(streetLine1 = event.value) }
            is PartnerSubmissionEvent.CityChanged -> _formState.update { it.copy(city = event.value) }
            is PartnerSubmissionEvent.ProvinceChanged -> _formState.update { it.copy(province = event.value) }
            is PartnerSubmissionEvent.PostalCodeChanged -> _formState.update { it.copy(postalCode = event.value) }
            is PartnerSubmissionEvent.OwnerFirstNameChanged -> _formState.update { it.copy(ownerFirstName = event.value) }
            is PartnerSubmissionEvent.OwnerLastNameChanged -> _formState.update { it.copy(ownerLastName = event.value) }
            is PartnerSubmissionEvent.OwnerEmailChanged -> _formState.update { it.copy(ownerEmail = event.value) }
            is PartnerSubmissionEvent.OwnerPhoneChanged -> _formState.update { it.copy(ownerPhone = event.value) }

            is PartnerSubmissionEvent.UseLoginEmailChanged -> _formState.update { it.copy(useLoginEmail = event.isChecked) }
            is PartnerSubmissionEvent.BusinessEmailChanged -> _formState.update { it.copy(businessEmail = event.value) }

            is PartnerSubmissionEvent.BankCodeChanged -> _formState.update { it.copy(bankCode = event.value) }
            is PartnerSubmissionEvent.BankNameChanged -> _formState.update { it.copy(bankName = event.value) }
            is PartnerSubmissionEvent.AccountNumberChanged -> _formState.update { it.copy(accountNumber = event.value) }
            is PartnerSubmissionEvent.AccountHolderNameChanged -> _formState.update { it.copy(accountHolderName = event.value) }

            is PartnerSubmissionEvent.KycFileSelected -> {
                _formState.update {
                    val currentList = it.selectedKycDocuments.toMutableList()
                    val existingIndex = currentList.indexOfFirst { pair -> pair.first == event.type }
                    if (existingIndex != -1) {
                        currentList[existingIndex] = event.type to event.uri
                    } else {
                        currentList.add(event.type to event.uri)
                    }
                    it.copy(selectedKycDocuments = currentList)
                }
            }

            is PartnerSubmissionEvent.RemoveKycFile -> {
                _formState.update {
                    it.copy(selectedKycDocuments = it.selectedKycDocuments.filter { pair -> pair.first != event.type })
                }
            }

            is PartnerSubmissionEvent.Submit -> submitRegistration(merchantType = merchantType)
            is PartnerSubmissionEvent.DismissError -> _uiState.value = PartnerSubmissionUiState.Idle
        }
    }

    private fun submitRegistration(merchantType: String) {
        Log.d("PartnerSubmissionViewModel", merchantType)
        val form = _formState.value

        val hasRequiredKtp = form.selectedKycDocuments.any { it.first == "AUTHORIZED_PERSON_KTP_DOCUMENT" }
        if (!hasRequiredKtp) {
            _uiState.value = PartnerSubmissionUiState.Error("Owner/Director KTP is required!")
            return
        }

        if (form.bankCode.isBlank() || form.bankName.isBlank() ||
            form.accountNumber.isBlank() || form.accountHolderName.isBlank()
        ) {
            _uiState.value = PartnerSubmissionUiState.Error("Please fill in all bank account details!")
            return
        }

        _uiState.value = PartnerSubmissionUiState.Loading

        viewModelScope.launch {
            val kycDocuments = form.selectedKycDocuments.map { (type, _) ->
                KycDocument(
                    type = type,
                    country = form.countryOperation,
                    fileId = "" // Filled in data layer after upload
                )
            }

            val accountHolderData = CreateAccountHolderInput(
                businessDetail = BusinessDetail(
                    type = form.businessType,
                    legalName = form.legalName,
                    tradingName = form.tradingName,
                    description = form.description,
                    industryCategory = form.industryCategory,
                    countryOfOperation = form.countryOperation,
                ),
                address = BusinessAddress(
                    streetLine1 = form.streetLine1,
                    city = form.city,
                    country = form.countryOperation,
                    provinceState = form.province,
                    postalCode = form.postalCode
                ),
                individualDetails = listOf(
                    IndividualDetail(
                        givenNames = form.ownerFirstName,
                        surname = form.ownerLastName,
                        email = form.ownerEmail,
                        phoneNumber = form.ownerPhone,
                        type = "PIC",
                        role = form.ownerRole
                    )
                ),
                kycDocuments = kycDocuments,
                email = form.ownerEmail
            )

            val bankAccount = BankAccount(
                bankCode = form.bankCode,
                bankName = form.bankName,
                accountNumber = form.accountNumber,
                accountHolderName = form.accountHolderName,
                isPrimary = form.isPrimary
            )

            val requestInput = RoleApplicationInput(
                useLoginEmail = form.useLoginEmail,
                businessEmail = if (form.useLoginEmail) null else form.businessEmail,
                businessPayload = accountHolderData,
                merchantType = merchantType,
                bankAccounts = bankAccount
            )

            val kycFiles = form.selectedKycDocuments.map { (type, uri) ->
                val fileName = "${type.lowercase()}.png"
                fileName to uri
            }

            val result = registerBusinessUseCase(
                input = requestInput,
                kycFiles = kycFiles
            )

            when (result) {
                is DataResult.Success -> {
                    _uiState.value = PartnerSubmissionUiState.Success(result.data)
                }
                is DataResult.Error -> {
                    _uiState.value = PartnerSubmissionUiState.Error(result.error)
                }
                else -> Unit
            }
        }
    }
}
