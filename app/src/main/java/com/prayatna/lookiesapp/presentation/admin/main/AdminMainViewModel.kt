package com.prayatna.lookiesapp.presentation.admin.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMainViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel(){
    private val _logoutStatus = MutableStateFlow<DataResult<Any>>(DataResult.Idle)
    val logoutStatus = _logoutStatus.asStateFlow()

    fun logout() {
        _logoutStatus.value = DataResult.Loading
        viewModelScope.launch {
           _logoutStatus.value = authRepository.logout()
        }
    }

}