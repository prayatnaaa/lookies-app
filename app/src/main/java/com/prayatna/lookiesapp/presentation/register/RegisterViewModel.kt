package com.prayatna.lookiesapp.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.repository.AuthRepository
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository): ViewModel() {

        var emailValue by mutableStateOf("")
            private set

        var passwordValue by mutableStateOf("")
            private set

        private val _registerStatus = MutableStateFlow<DataResult<String>>(DataResult.Idle)
        val registerStatus = _registerStatus.asStateFlow()

        fun onEmailChange(emailValue: String) {
            this.emailValue = emailValue
        }

        fun onPasswordChange(passwordValue: String) {
            this.passwordValue = passwordValue
        }

        fun onSignUp() {
            _registerStatus.value = DataResult.Loading
            viewModelScope.launch {
                val result =
                    authRepository.signUp(
                    email = emailValue,
                    password = passwordValue
                )
                _registerStatus.value = result
            }
        }
}