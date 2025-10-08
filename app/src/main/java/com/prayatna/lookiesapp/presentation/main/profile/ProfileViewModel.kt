package com.prayatna.lookiesapp.presentation.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.remote.mapper.asDomainModel
import com.prayatna.lookiesapp.data.remote.model.Profile
import com.prayatna.lookiesapp.data.repository.AuthRepository
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository
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

    private fun getProfile() = viewModelScope.launch {
        repository.getProfile().collect { result ->
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

    init {
        getProfile()
    }
}