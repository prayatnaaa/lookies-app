package com.prayatna.lookiesapp.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.auth.RegisterInput
import com.prayatna.lookiesapp.domain.model.auth.RegisterOutput
import com.prayatna.lookiesapp.domain.usecase.auth.RegisterUseCase
import com.prayatna.lookiesapp.presentation.register.events.RegisterEvent
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    var fullName by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var verifyPassword by mutableStateOf("")
        private set

    private val _registerStatus =
        MutableStateFlow<DataResult<RegisterOutput>>(DataResult.Idle)
    val registerStatus = _registerStatus.asStateFlow()

    private val _events = MutableSharedFlow<RegisterEvent>()
    val events = _events.asSharedFlow()

    fun onFullNameChange(value: String) {
        fullName = value
    }

    fun onEmailChange(value: String) {
        email = value
    }

    fun onPasswordChange(value: String) {
        password = value
    }

    fun onVerifyPasswordChange(value: String) {
        verifyPassword = value
    }

    fun onSignUp() {
        val input = RegisterInput(
            fullName = fullName.trim(),
            email = email.trim(),
            password = password,
            verifyPassword = verifyPassword
        )

        viewModelScope.launch {
            _registerStatus.value = DataResult.Loading

            when (val result = registerUseCase(input)) {
                is DataResult.Success -> {
                    _registerStatus.value = result
                    _events.emit(
                        RegisterEvent.ShowSuccessDialog(
                            result.data.message
                        )
                    )
                }

                is DataResult.Error -> {
                    _registerStatus.value = result
                    _events.emit(
                        RegisterEvent.ShowErrorDialog(result.error)
                    )
                }

                else -> Unit
            }
        }
    }
}
