package com.prayatna.lookiesapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.user.Profile
import com.prayatna.lookiesapp.domain.usecase.user.GetProfileUseCase
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _profileState = MutableStateFlow<DataResult<Profile>>(DataResult.Idle)
    val profileState: StateFlow<DataResult<Profile>> = _profileState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        getProfileUseCase()
            .onEach { result ->
                _profileState.value = result
            }
            .launchIn(viewModelScope)
    }

    fun refreshProfile() {
        loadProfile()
    }
}
