package com.prayatna.lookiesapp.presentation.user.createUserAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.user.CreateUserAddressInput
import com.prayatna.lookiesapp.domain.usecase.user.CreateUserAddressUseCase
import com.prayatna.lookiesapp.presentation.user.createUserAddress.state.CreateUserAddressEvent
import com.prayatna.lookiesapp.presentation.user.createUserAddress.state.CreateUserAddressFormState
import com.prayatna.lookiesapp.presentation.user.createUserAddress.state.CreateUserAddressUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserAddressViewModel @Inject constructor(
    private val createUserAddressUseCase: CreateUserAddressUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(CreateUserAddressUiState())
    val uiState: StateFlow<CreateUserAddressUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(CreateUserAddressFormState())
    val formState: StateFlow<CreateUserAddressFormState> = _formState.asStateFlow()

    fun onEvent(event: CreateUserAddressEvent) {
        when (event) {
            is CreateUserAddressEvent.OnAddressLineChange ->
                update { copy(addressLine = event.addressLine) }
            is CreateUserAddressEvent.OnCityChange ->
                update { copy(city = event.city) }
            is CreateUserAddressEvent.OnNameChange ->
                update { copy(name = event.name) }
            is CreateUserAddressEvent.OnPhoneNumberChange ->
                update { copy(phoneNumber = event.phoneNumber) }
            is CreateUserAddressEvent.OnPostalCodeChange ->
                update { copy(postalCode = event.postalCode) }
            is CreateUserAddressEvent.OnProvinceChange ->
                update { copy(province = event.province) }
            is CreateUserAddressEvent.OnNotesChange ->
                update { copy(notes = event.notes) }
            is CreateUserAddressEvent.OnIsDefaultChange ->
                update { copy(isDefault = event.isDefault) }
            CreateUserAddressEvent.OnSubmit -> submitAddress()
        }
    }

    private fun submitAddress() {
        val form = _formState.value

        // Validate required fields
        if (form.name.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Recipient name is required")
            return
        }
        if (form.phoneNumber.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Phone number is required")
            return
        }
        if (form.addressLine.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Address is required")
            return
        }
        if (form.province.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Province is required")
            return
        }
        if (form.city.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "City is required")
            return
        }
        if (form.postalCode.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Postal code is required")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val input = CreateUserAddressInput(
                userId = "",  // Will be filled by SupabaseUserService from auth
                name = form.name,
                addressLine = form.addressLine,
                province = form.province,
                phoneNumber = form.phoneNumber,
                postalCode = form.postalCode,
                city = form.city,
                isDefault = form.isDefault,
                notes = form.notes?.ifBlank { null }
            )

            when (val result = createUserAddressUseCase(input)) {
                is DataResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "Address added successfully!"
                    )
                }
                is DataResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.error
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun update(reducer: CreateUserAddressFormState.() -> CreateUserAddressFormState) {
        _formState.value = _formState.value.reducer()
    }

}