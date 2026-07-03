package com.prayatna.lookiesapp.presentation.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.domain.usecase.user.GetThemeUseCase
import com.prayatna.lookiesapp.domain.usecase.user.SetThemeUseCase
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
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
): ViewModel() {

    var isSuccess by mutableStateOf(false)
        private set
    var isError by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(false)
        private set

    var errorMsg by mutableStateOf("")

    private val _logoutStatus = MutableStateFlow<DataResult<Any>>(DataResult.Idle)
    val logoutStatus = _logoutStatus.asStateFlow()

    val isDarkMode: StateFlow<Boolean> = getThemeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun toggleDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            setThemeUseCase(isDarkMode)
        }
    }

    fun logout() {
        _logoutStatus.value = DataResult.Loading
        viewModelScope.launch {
            _logoutStatus.value = authRepository.logout()
        }
    }
}
