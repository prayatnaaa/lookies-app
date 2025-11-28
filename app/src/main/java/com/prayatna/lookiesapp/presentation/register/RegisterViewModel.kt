package com.prayatna.lookiesapp.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.presentation.register.events.RegisterEvent
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

        var emailValue by mutableStateOf("")
            private set

        var passwordValue by mutableStateOf("")
            private set

        private val _registerStatus = MutableStateFlow<DataResult<UserInfo?>>(DataResult.Idle)
        val registerStatus = _registerStatus.asStateFlow()

        private val _events = MutableSharedFlow<RegisterEvent>()
        val events = _events.asSharedFlow()

        fun onEmailChange(emailValue: String) {
            this.emailValue = emailValue
        }

        fun onPasswordChange(passwordValue: String) {
            this.passwordValue = passwordValue
        }

    fun onSignUp() {
        viewModelScope.launch {
            _registerStatus.value = DataResult.Loading

            when (val result = authRepository.signUp(emailValue, passwordValue)) {
                is DataResult.Success -> {
                    _events.emit(RegisterEvent.ShowSnackbar("Register success!"))
                    _events.emit(RegisterEvent.NavigateToLogin)
                    _registerStatus.value = DataResult.Success(result.data)
                }

                is DataResult.Error -> {
                    _events.emit(RegisterEvent.ShowSnackbar(result.error))
                    _registerStatus.value = DataResult.Error(result.error)
                }
                else -> Unit
            }
        }
    }
}