package com.prayatna.lookiesapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.remote.dto.response.auth.LoginResponse
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreference: UserPreference): ViewModel() {

    var emailValue by mutableStateOf("")
        private set

    var passwordValue by mutableStateOf("")
        private set

    private val _loginStatus = MutableStateFlow<DataResult<LoginResponse>>(DataResult.Idle)
    val loginStatus = _loginStatus.asStateFlow()

    private val _sessionStatus = MutableStateFlow<DataResult<Boolean>>(DataResult.Idle)
    val sessionStatus = _sessionStatus.asStateFlow()

    fun onEmailChange(emailValue: String) {
       this.emailValue = emailValue
    }

    fun onPasswordChange(passwordValue: String) {
        this.passwordValue = passwordValue
    }

    fun onSignIn() {
        _loginStatus.value = DataResult.Loading
        viewModelScope.launch {
            val result = authRepository.signIn(
                email = emailValue,
                password = passwordValue
            )
            _loginStatus.value = result
        }
    }

    val roleState: StateFlow<String> = userPreference.getRole()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    fun isSessionActive() {
        viewModelScope.launch {
            _sessionStatus.value = authRepository.isSessionActive()
        }
    }
}