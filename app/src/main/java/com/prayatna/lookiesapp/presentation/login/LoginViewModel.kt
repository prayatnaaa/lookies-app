package com.prayatna.lookiesapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.remote.dto.response.auth.LoginResponse
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.domain.usecase.auth.ListenUserSessionUseCase
import com.prayatna.lookiesapp.presentation.login.state.AuthState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreference: UserPreference,
    private val listenUserSessionUseCase: ListenUserSessionUseCase
) : ViewModel() {

    var emailValue by mutableStateOf("")
        private set

    var passwordValue by mutableStateOf("")
        private set

    private val _loginStatus =
        MutableStateFlow<DataResult<LoginResponse>>(DataResult.Idle)
    val loginStatus = _loginStatus.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun onEmailChange(emailValue: String) {
        this.emailValue = emailValue
    }

    fun onPasswordChange(passwordValue: String) {
        this.passwordValue = passwordValue
    }

    private fun resetForm() {
        emailValue = ""
        passwordValue = ""
    }

    fun resetLoginState() {
        emailValue = ""
        passwordValue = ""
        _authState.value = AuthState.Unauthenticated
    }

    fun onSignIn() {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            when (val result = authRepository.signIn(emailValue, passwordValue)) {

                is DataResult.Success -> {
                    val role = result.data.role
                    resetForm()
                    _authState.value = AuthState.Authenticated(role)
                }

                is DataResult.Error -> {
                    _authState.value = AuthState.Error(result.error)
                }

                else -> Unit
            }
        }
    }

    fun observeSession() {
        viewModelScope.launch {
            when (val result = listenUserSessionUseCase()) {
                is DataResult.Error -> {
                    _authState.value = AuthState.Error(result.error)
                }
                DataResult.Idle -> TODO()
                DataResult.Loading -> TODO()
                is DataResult.Success -> {
                    val session = result.data

                    session.collect { sessionStatus ->
                        when (sessionStatus) {
                            is SessionStatus.Authenticated -> {
                                loadAuthenticatedUser()
                            }
                            is SessionStatus.NotAuthenticated -> {
                                _authState.value = AuthState.Unauthenticated
                            }
                            else -> {}
                        }

                    }
                }
            }
        }
    }

    fun isSessionActive() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            when (val sessionResult = authRepository.isSessionActive()) {

                is DataResult.Success -> {
                    if (sessionResult.data) {
                        loadAuthenticatedUser()
                    } else {
                        _authState.value = AuthState.Unauthenticated
                    }
                }

                is DataResult.Error -> {
                    _authState.value = AuthState.Error(sessionResult.error)
                }

                else -> Unit
            }
        }
    }

    private fun loadAuthenticatedUser() {
        viewModelScope.launch {
            val role = userPreference.getRole().firstOrNull()

            if (role.isNullOrBlank()) {
                _authState.value = AuthState.Unauthenticated
            } else {
                _authState.value = AuthState.Authenticated(role)
            }
        }
    }

}