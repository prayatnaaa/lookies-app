package com.prayatna.lookiesapp.presentation.user.partnerapplication

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.user.PartnerSubmissionParams
import com.prayatna.lookiesapp.domain.usecase.user.AddPartnerSubmissionUseCase
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
    private val partnerApplicationUseCase: AddPartnerSubmissionUseCase
): ViewModel() {
    private val _addPartnerSubmissionState = MutableStateFlow(PartnerSubmissionState())
    val addPartnerSubmissionState: StateFlow<PartnerSubmissionState> = _addPartnerSubmissionState

    private val _addPartnerSubmissionFormState = MutableStateFlow(PartnerSubmissionFormState())
    val addPartnerSubmissionFormState: StateFlow<PartnerSubmissionFormState> = _addPartnerSubmissionFormState

    fun onPartnerNameChanged(value: String) {
        _addPartnerSubmissionFormState.update { it.copy(partnerName = value) }
    }

    fun onPartnerTypeChange(value: String) {
        _addPartnerSubmissionFormState.update { it.copy(partnerType = value) }
    }

    fun onLocNameChange(value: String) {
        _addPartnerSubmissionFormState.update { it.copy(locName = value) }
    }

    fun onLocUrlChange(value: String) {
        _addPartnerSubmissionFormState.update { it.copy(locUrl = value) }
    }

    fun onPortofolioChange(value: String) {
        _addPartnerSubmissionFormState.update { it.copy(portfolioLink = value) }
    }

    fun onImageLogoChange(value: Uri) {
        _addPartnerSubmissionFormState.update { it.copy(imageLogo = value) }
    }

    fun submitPartnerSubmission() {
        val form = _addPartnerSubmissionFormState.value

        if (form.partnerName.isBlank() ||
            form.partnerType.isBlank() || form.imageLogo == null
        ) {
            _addPartnerSubmissionState.update {
                it.copy(error = "Please complete all required fields")
            }
            return
        }

        val request = PartnerSubmissionParams(
            partnerName = form.partnerName,
            partnerType = form.partnerType,
            locName = form.locName,
            locUrl = form.locUrl,
            portfolioLink = form.portfolioLink,
            imageLogo = form.imageLogo
        )

        viewModelScope.launch {
            _addPartnerSubmissionState.update { it.copy(isLoading = true, error = null) }

            when (val result = partnerApplicationUseCase(request)) {
                is DataResult.Success -> _addPartnerSubmissionState.update {
                    it.copy(isLoading = false, success = result.data)
                }

                is DataResult.Error -> _addPartnerSubmissionState.update {
                    it.copy(isLoading = false, error = result.error)
                }

                else -> Unit
            }
        }
    }
}