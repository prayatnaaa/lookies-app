package com.prayatna.lookiesapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.remote.dto.response.auth.LoginResponse
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.presentation.components.SessionState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreference: UserPreference
) : ViewModel() {

    var emailValue by mutableStateOf("")
        private set

    var passwordValue by mutableStateOf("")
        private set

    private val _loginStatus =
        MutableStateFlow<DataResult<LoginResponse>>(DataResult.Idle)
    val loginStatus = _loginStatus.asStateFlow()

    private val _sessionState =
        MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private val _roleState =
        MutableStateFlow<String?>(null)
    val roleState: StateFlow<String?> = _roleState.asStateFlow()

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

            if (result is DataResult.Success) {
                _sessionState.value = SessionState.Authenticated
                loadUserRole()
            }
        }
    }

    fun isSessionActive() {
        viewModelScope.launch {
            _sessionState.value = SessionState.Loading

            when (val result = authRepository.isSessionActive()) {

                is DataResult.Success -> {
                    if (result.data) {
                        _sessionState.value = SessionState.Authenticated
                        loadUserRole()
                    } else {
                        _sessionState.value = SessionState.Unauthenticated
                    }
                }

                is DataResult.Error -> {
                    _sessionState.value =
                        SessionState.Error(result.error)
                }

                DataResult.Idle,
                DataResult.Loading -> {
                    _sessionState.value = SessionState.Loading
                }
            }
        }
    }

    private fun loadUserRole() {
        viewModelScope.launch {
            userPreference.getRole().collect { role ->
                _roleState.value =
                    role.takeIf { it.isNotBlank() }
            }
        }
    }
}