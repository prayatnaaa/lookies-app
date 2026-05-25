package com.prayatna.lookiesapp.presentation.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.remote.dto.response.auth.LoginResponse
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.domain.usecase.auth.ListenUserSessionUseCase
import com.prayatna.lookiesapp.domain.usecase.auth.LoginUseCase
import com.prayatna.lookiesapp.domain.usecase.user.GetFcmTokenUseCase
import com.prayatna.lookiesapp.presentation.login.state.AuthEvent
import com.prayatna.lookiesapp.presentation.login.state.AuthState
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.worker.FcmTokenScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val listenUserSessionUseCase: ListenUserSessionUseCase,
    private val getFcmTokenUseCase: GetFcmTokenUseCase,
//    private val getRoleUseCase: GetRoleUseCase,
    private val fcmTokenScheduler: FcmTokenScheduler,
    private val authRepository: AuthRepository
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

    private val _eventFlow = MutableSharedFlow<AuthEvent>(
        replay = 1
    )
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        observeSession()
    }

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
            when (val result = loginUseCase(emailValue, passwordValue)) {

                is DataResult.Success -> {
                    Log.d("SignIn", "Success " + result.data)
                    resetForm()
                }

                is DataResult.Error -> {
                    Log.d("SignIn", "Error " + result.error)
                    _eventFlow.emit(AuthEvent.ShowError(result.error))
                    _authState.value = AuthState.Unauthenticated
                }

                else -> Unit
            }
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            when (val result = listenUserSessionUseCase()) {
                is DataResult.Error -> {
                    _eventFlow.emit(AuthEvent.ShowError(result.error))
                }
                is DataResult.Success -> {
                    val session = result.data

                    session.collectLatest { sessionStatus ->
                        when (sessionStatus) {
                            is SessionStatus.Authenticated -> {
                                Log.d("SignIn", "Authenticated")
                                loadAuthenticatedUser()
                            }
                            is SessionStatus.NotAuthenticated -> {
                                Log.d("SignIn", "Not authenticated")
                                _authState.value = AuthState.Unauthenticated
                            }
                            else -> Unit
                        }
                    }
                }
                else -> Unit
            }
        }
    }

    private fun loadAuthenticatedUser() {
        viewModelScope.launch {
            val role = authRepository.getRole()

            _authState.value = if (role.isBlank()) {
                AuthState.Unauthenticated
            } else {
                AuthState.Authenticated(role)
            }

            launch(Dispatchers.IO) {
                val token = getFcmTokenUseCase()
                token?.let { fcmTokenScheduler.enqueue(it) }
            }
        }
    }

}