package com.prayatna.lookiesapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _state = MutableStateFlow(MainUiState())
    val state = _state.asStateFlow()

    fun getUser(forceRefresh: Boolean = false) {
        if (!forceRefresh && _state.value.user != null) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = userRepository.getUser()) {
                is DataResult.Error -> _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = result.error
                )
                is DataResult.Success -> _state.value = _state.value.copy(
                    isLoading = false,
                    user = result.data
                )
                else -> {}
            }
        }
    }

    fun retry() = getUser( forceRefresh = true)

}