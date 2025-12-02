package com.prayatna.lookiesapp.presentation.user.partnerapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.user.PartnerSubmissionParams
import com.prayatna.lookiesapp.domain.usecase.user.SubmitPartnerSubmissionUseCase
import com.prayatna.lookiesapp.presentation.user.partnerapplication.event.PartnerApplicationEvent
import com.prayatna.lookiesapp.presentation.user.partnerapplication.state.PartnerSubmissionFormState
import com.prayatna.lookiesapp.presentation.user.partnerapplication.state.PartnerSubmissionState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerApplicationViewModel @Inject constructor(
    private val submitUseCase: SubmitPartnerSubmissionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PartnerSubmissionState())
    val state: StateFlow<PartnerSubmissionState> = _state

    private val _form = MutableStateFlow(PartnerSubmissionFormState())
    val form: StateFlow<PartnerSubmissionFormState> = _form

    fun onEvent(event: PartnerApplicationEvent) {
        when (event) {

            is PartnerApplicationEvent.LocNameChanged ->
                updateForm { copy(locName = event.value) }

            is PartnerApplicationEvent.LocUrlChanged ->
                updateForm { copy(locUrl = event.value) }

            is PartnerApplicationEvent.PartnerNameChanged ->
                updateForm { copy(partnerName = event.value) }

            is PartnerApplicationEvent.PartnerTypeChanged ->
                updateForm { copy(partnerType = event.value) }

            is PartnerApplicationEvent.PartnerLogoChanged ->
                updateForm { copy(partnerLogo = event.value) }

            is PartnerApplicationEvent.PortfolioLinkChanged ->
                updateForm { copy(partnerPortfolioLink = event.value) }

            is PartnerApplicationEvent.KtpFileChanged ->
                updateForm { copy(ktpFile = event.value) }

            is PartnerApplicationEvent.BusinessLicenseFileChanged ->
                updateForm { copy(businessLicenseFile = event.value) }

            is PartnerApplicationEvent.BankNameChanged ->
                updateForm { copy(bankName = event.value) }

            is PartnerApplicationEvent.BankAccountNumberChanged ->
                updateForm { copy(bankAccountNumber = event.value) }

            is PartnerApplicationEvent.BankAccountHolderChanged ->
                updateForm { copy(bankAccountHolder = event.value) }

            PartnerApplicationEvent.Submit -> submit()
        }
    }

    private fun updateForm(updater: PartnerSubmissionFormState.() -> PartnerSubmissionFormState) {
        _form.update { it.updater() }
    }

    private fun submit() {
        val formValue = _form.value

        if (!formValue.isValid()) {
            _state.update { it.copy(error = "Please complete all required fields") }
            return
        }

        val params = formValue.toParams()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = submitUseCase(params)) {
                is DataResult.Success ->
                    _state.update { it.copy(isLoading = false, success = result.data) }

                is DataResult.Error ->
                    _state.update { it.copy(isLoading = false, error = result.error) }

               else -> Unit
            }
        }
    }
}

private fun PartnerSubmissionFormState.toParams() = PartnerSubmissionParams(
    locName = locName,
    locUrl = locUrl,
    partnerName = partnerName,
    partnerType = partnerType,
    partnerLogo = partnerLogo!!,
    partnerPortfolioLink = partnerPortfolioLink,
    ktpFile = ktpFile!!,
    businessLicenseFile = businessLicenseFile!!,
    bankName = bankName,
    bankAccountNumber = bankAccountNumber,
    bankAccountHolder = bankAccountHolder
)



private fun PartnerSubmissionFormState.isValid(): Boolean {
    return partnerName.isNotBlank() &&
            partnerType.isNotBlank() &&
            partnerLogo != null &&
            ktpFile != null &&
            businessLicenseFile != null &&
            bankName.isNotBlank() &&
            bankAccountNumber.isNotBlank() &&
            bankAccountHolder.isNotBlank()
}
