package com.prayatna.lookiesapp.presentation.user.createUserAddress.state

sealed class CreateUserAddressEvent {
    data class OnNameChange(val name: String) : CreateUserAddressEvent()
    data class OnPhoneNumberChange(val phoneNumber: String) : CreateUserAddressEvent()
    data class OnProvinceChange(val province: String) : CreateUserAddressEvent()
    data class OnCityChange(val city: String) : CreateUserAddressEvent()
    data class OnAddressLineChange(val addressLine: String) : CreateUserAddressEvent()
    data class OnPostalCodeChange(val postalCode: String) : CreateUserAddressEvent()
    data class OnNotesChange(val notes: String) : CreateUserAddressEvent()
    data class OnIsDefaultChange(val isDefault: Boolean) : CreateUserAddressEvent()
    data object OnSubmit : CreateUserAddressEvent()
}