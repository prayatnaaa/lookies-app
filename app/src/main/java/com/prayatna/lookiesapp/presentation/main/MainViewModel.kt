package com.prayatna.lookiesapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _role = MutableStateFlow("")
    val role = _role

    init {
        getRole()
    }

    private fun getRole() = viewModelScope.launch {
        _role.value = userRepository.getRole()
    }
}