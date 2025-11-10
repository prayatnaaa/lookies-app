package com.prayatna.lookiesapp.presentation.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.user.Profile
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
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

    fun logout() {
        _logoutStatus.value = DataResult.Loading
        viewModelScope.launch {
            _logoutStatus.value = authRepository.logout()
        }
    }
}