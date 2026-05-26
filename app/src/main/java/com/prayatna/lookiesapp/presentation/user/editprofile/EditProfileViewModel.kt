package com.prayatna.lookiesapp.presentation.user.editprofile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    private val _editProfileStatus = MutableStateFlow<DataResult<String>>(DataResult.Idle)
    val editProfileStatus = _editProfileStatus.asStateFlow()

    var fullNameValue by mutableStateOf("")
        private set
    fun onFullNameChange(fullNameValue: String) {
        this.fullNameValue = fullNameValue
    }

    var bioValue by mutableStateOf("")
        private set
    fun onBioChange(bioValue: String) {
        this.bioValue = bioValue
    }

    var addressValue by mutableStateOf("")
        private set
    fun onAddressChange(addressValue: String) {
        this.addressValue = addressValue
    }

    var usernameValue by mutableStateOf("")
        private set
    fun onUsernameChange(usernameValue: String) {
        this.usernameValue = usernameValue
    }

    private var originalUsername = ""
    private var originalFullName = ""
    private var originalAddress = ""
    private var originalBio = ""

    fun prefillProfile(username: String, fullName: String, address: String, bio: String) {
        this.usernameValue = username
        this.fullNameValue = fullName
        this.addressValue = address
        this.bioValue = bio

        originalUsername = username
        originalFullName = fullName
        originalAddress = address
        originalBio = bio
    }

    val isChanged: Boolean
        get() = usernameValue != originalUsername ||
                fullNameValue != originalFullName ||
                addressValue != originalAddress ||
                bioValue != originalBio

    fun onEditProfile () {
        _editProfileStatus.value = DataResult.Loading
        viewModelScope.launch {
            val result = userRepository.editProfile(
                fullName = fullNameValue,
                bio = bioValue,
                address = addressValue,
                username = usernameValue
            )

            _editProfileStatus.value = result
        }
    }
}