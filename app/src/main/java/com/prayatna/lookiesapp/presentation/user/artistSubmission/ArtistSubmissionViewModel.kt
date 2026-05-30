package com.prayatna.lookiesapp.presentation.user.artistSubmission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.user.Address
import com.prayatna.lookiesapp.domain.model.user.ArtistApplicationInput
import com.prayatna.lookiesapp.domain.model.user.BankAccount
import com.prayatna.lookiesapp.domain.model.user.KycDocument
import com.prayatna.lookiesapp.domain.usecase.payment.GetPayoutChannelsUseCase
import com.prayatna.lookiesapp.domain.usecase.user.BecomeArtistUseCase
import com.prayatna.lookiesapp.presentation.user.artistSubmission.state.ArtistSubmissionEvent
import com.prayatna.lookiesapp.presentation.user.artistSubmission.state.ArtistSubmissionFormState
import com.prayatna.lookiesapp.presentation.user.artistSubmission.state.ArtistSubmissionUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistSubmissionViewModel @Inject constructor(
    private val becomeArtistUseCase: BecomeArtistUseCase,
    private val getPayoutChannelsUseCase: GetPayoutChannelsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArtistSubmissionUiState>(ArtistSubmissionUiState.Idle)
    val uiState: StateFlow<ArtistSubmissionUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(ArtistSubmissionFormState())
    val formState: StateFlow<ArtistSubmissionFormState> = _formState.asStateFlow()

    private val _effect = MutableSharedFlow<ArtistSubmissionEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadPayoutChannels()
    }

    private fun loadPayoutChannels() {
        viewModelScope.launch {
            when (val result = getPayoutChannelsUseCase()) {
                is DataResult.Success -> {
                    _uiState.value = ArtistSubmissionUiState.MetaLoaded(result.data)
                }
                else -> Unit
            }
        }
    }

    fun onEvent(event: ArtistSubmissionEvent) {
        when (event) {
            is ArtistSubmissionEvent.OnBack -> {
                viewModelScope.launch {
                    _effect.emit(ArtistSubmissionEffect.NavigateBack)
                }
            }

            is ArtistSubmissionEvent.Submit -> submitRegistration()

            is ArtistSubmissionEvent.OnSelectBankClicked -> {
                viewModelScope.launch {
                    _effect.emit(ArtistSubmissionEffect.NavigateToSelectBank)
                }
            }

            else -> updateForm(event)
        }
    }

    private fun submitRegistration() {
        val form = _formState.value

        if (form.kycFileBytes == null) {
            _uiState.value = ArtistSubmissionUiState.Error("Input KYC file!")
            return
        }

        if (form.fullName.isBlank() || form.phoneNumber.isBlank()) {
            _uiState.value = ArtistSubmissionUiState.Error("Please fill in required personal details (Full Name, Phone)!")
            return
        }

        if (form.bankCode.isBlank() || form.bankName.isBlank() ||
            form.accountNumber.isBlank() || form.accountHolderName.isBlank()
        ) {
            _uiState.value = ArtistSubmissionUiState.Error("Please fill in all bank account details!")
            return
        }

        _uiState.value = ArtistSubmissionUiState.Loading

        viewModelScope.launch {
            val address = Address(
                country = form.country.ifBlank { "ID" },
                city = form.city,
                streetLine1 = form.streetLine1,
                streetLine2 = form.streetLine2.takeIf { it.isNotBlank() },
                district = form.district,
                subDistrict = form.subDistrict,
                provinceState = form.province,
                postalCode = form.postalCode
            )

            val bankAccount = BankAccount(
                bankCode = form.bankCode,
                bankName = form.bankName,
                accountNumber = form.accountNumber,
                accountHolderName = form.accountHolderName
            )

            val requestInput = ArtistApplicationInput(
                fullName = form.fullName,
                displayName = form.displayName.takeIf { it.isNotBlank() },
                bio = form.bio.takeIf { it.isNotBlank() },
                phoneNumber = form.phoneNumber,
                nationality = form.nationality.takeIf { it.isNotBlank() },
                placeOfBirth = form.placeOfBirth.takeIf { it.isNotBlank() },
                dateOfBirth = form.dateOfBirth.takeIf { it.isNotBlank() },
                gender = form.gender,
                website = form.website.takeIf { it.isNotBlank() },
                country = form.country.takeIf { it.isNotBlank() },
                address = address,
                bankAccount = bankAccount,
                kycDocuments = listOf(
                    KycDocument(
                        type = form.kycFileType,
                        country = form.country.ifBlank { "ID" },
                        fileId = "" // Filled in data layer
                    )
                )
            )

            val result = becomeArtistUseCase(
                input = requestInput,
                kycFile = form.kycFileBytes,
                fileName = form.kycFileName
            )

            when (result) {
                is DataResult.Success -> {
                    _uiState.value = ArtistSubmissionUiState.Success(result.data)
                    emitEffect(ArtistSubmissionEffect.NavigateToSuccess)
                }
                is DataResult.Error -> {
                    _uiState.value = ArtistSubmissionUiState.Error(result.error)
                    emitEffect(ArtistSubmissionEffect.ShowSnackbar(result.error))
                }
                else -> Unit
            }
        }
    }

    private fun emitEffect(effect: ArtistSubmissionEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun updateForm(event: ArtistSubmissionEvent) {
        _formState.update {
            when (event) {
                is ArtistSubmissionEvent.FullNameChanged -> it.copy(fullName = event.value)
                is ArtistSubmissionEvent.DisplayNameChanged -> it.copy(displayName = event.value)
                is ArtistSubmissionEvent.BioChanged -> it.copy(bio = event.value)
                is ArtistSubmissionEvent.PhoneNumberChanged -> it.copy(phoneNumber = event.value)
                is ArtistSubmissionEvent.NationalityChanged -> it.copy(nationality = event.value)
                is ArtistSubmissionEvent.PlaceOfBirthChanged -> it.copy(placeOfBirth = event.value)
                is ArtistSubmissionEvent.DateOfBirthChanged -> it.copy(dateOfBirth = event.value)
                is ArtistSubmissionEvent.GenderChanged -> it.copy(gender = event.value)
                is ArtistSubmissionEvent.WebsiteChanged -> it.copy(website = event.value)
                is ArtistSubmissionEvent.CountryChanged -> it.copy(country = event.value)

                is ArtistSubmissionEvent.AddressChanged -> it.copy(streetLine1 = event.value)
                is ArtistSubmissionEvent.AddressLine2Changed -> it.copy(streetLine2 = event.value)
                is ArtistSubmissionEvent.CityChanged -> it.copy(city = event.value)
                is ArtistSubmissionEvent.ProvinceChanged -> it.copy(province = event.value)
                is ArtistSubmissionEvent.DistrictChanged -> it.copy(district = event.value)
                is ArtistSubmissionEvent.SubDistrictChanged -> it.copy(subDistrict = event.value)
                is ArtistSubmissionEvent.PostalCodeChanged -> it.copy(postalCode = event.value)

                is ArtistSubmissionEvent.BankCodeChanged -> it.copy(bankCode = event.value)
                is ArtistSubmissionEvent.BankNameChanged -> it.copy(bankName = event.value)
                is ArtistSubmissionEvent.AccountNumberChanged -> it.copy(accountNumber = event.value)
                is ArtistSubmissionEvent.AccountHolderNameChanged -> it.copy(accountHolderName = event.value)

                is ArtistSubmissionEvent.KycFileSelected -> it.copy(kycFileBytes = event.uri)

                else -> it
            }
        }
    }
}
