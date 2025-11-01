package com.prayatna.lookiesapp.presentation.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.remote.mapper.asDomainModel
import com.prayatna.lookiesapp.domain.model.Profile
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
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    var isSuccess by mutableStateOf(false)
        private set
    var isError by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(false)
        private set

    var errorMsg by mutableStateOf("")

    var user by mutableStateOf<Profile?>(null)
        private set

    private val _logoutStatus = MutableStateFlow<DataResult<Any>>(DataResult.Idle)
    val logoutStatus = _logoutStatus.asStateFlow()

    private fun getProfile() = viewModelScope.launch {
        userRepository.getProfile().collect { result ->
            when (result) {
                is DataResult.Error -> {
                    isError = true
                    isSuccess = false
                    isLoading = false
                    errorMsg = result.error
                }
                DataResult.Loading -> {
                    isLoading = true
                    isSuccess = false
                    isError = false
                }
                is DataResult.Success-> {
                    isSuccess = true
                    isError = false
                    isLoading = false
                    user = result.data.asDomainModel()
                }
                else -> {}
            }
        }
    }

    fun logout() {
        _logoutStatus.value = DataResult.Loading
        viewModelScope.launch {
            _logoutStatus.value = authRepository.logout()
        }
    }

    init {
        getProfile()
    }
}