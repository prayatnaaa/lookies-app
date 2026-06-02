package com.prayatna.lookiesapp.presentation.publicMerchantProfile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.event.GetEventsUseCase
import com.prayatna.lookiesapp.domain.usecase.merchant.GetPublicMerchantProfileUseCase
import com.prayatna.lookiesapp.presentation.publicMerchantProfile.state.PublicMerchantProfileEffect
import com.prayatna.lookiesapp.presentation.publicMerchantProfile.state.PublicMerchantProfileEvent
import com.prayatna.lookiesapp.presentation.publicMerchantProfile.state.PublicMerchantProfileUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicMerchantProfileViewModel @Inject constructor(
    private val getPublicMerchantProfileUseCase: GetPublicMerchantProfileUseCase,
    private val getEventsUseCase: GetEventsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val businessId: String = savedStateHandle["businessId"] ?: ""

    private val _uiState = MutableStateFlow(PublicMerchantProfileUiState(businessId = businessId))
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PublicMerchantProfileEffect>()
    val effect = _effect.asSharedFlow()

    init {
        if (businessId.isNotEmpty()) {
            loadData(businessId)
        }
    }

    fun onEvent(event: PublicMerchantProfileEvent) {
        when (event) {
            PublicMerchantProfileEvent.OnBackClicked -> {
                viewModelScope.launch { _effect.emit(PublicMerchantProfileEffect.NavigateBack) }
            }
            is PublicMerchantProfileEvent.OnEventClicked -> {
                viewModelScope.launch { _effect.emit(PublicMerchantProfileEffect.NavigateToEventDetail(event.eventId)) }
            }
        }
    }

    private fun loadData(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val profileDeferred = async { getPublicMerchantProfileUseCase(id) }
            val eventsDeferred = async { getEventsUseCase(organizerId = id, status = "ongoing") }

            val profileResult = profileDeferred.await()
            val eventsResult = eventsDeferred.await()
            Log.d("PublicMerchant", eventsResult.toString())

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    merchant = if (profileResult is DataResult.Success) profileResult.data else null,
                    ongoingEvents = if (eventsResult is DataResult.Success) eventsResult.data else emptyList(),
                    errorMessage = when {
                        profileResult is DataResult.Error -> profileResult.error
                        eventsResult is DataResult.Error -> eventsResult.error
                        else -> null
                    }
                )
            }
        }
    }
}
