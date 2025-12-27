package com.prayatna.lookiesapp.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.domain.usecase.event.GetEventsUseCase
import com.prayatna.lookiesapp.domain.usecase.painting.GetPaintingUseCase
import com.prayatna.lookiesapp.presentation.main.home.state.HomeUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val getPaintingsUseCase: GetPaintingUseCase,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
        loadPaintings()
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            userPreference.getProfile().collect { user ->
                _uiState.update {
                    it.copy(
                        user = user
                    )
                }
            }
        }
    }

    private fun loadEvents(forceRefresh: Boolean = false) {
        if (!forceRefresh && _uiState.value.events.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingEvents = true,
                    errorMessage = null
                )
            }

            when (val result = getEventsUseCase()) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingEvents = false,
                            events = result.data
                        )
                    }
                }

                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingEvents = false,
                            errorMessage = result.error
                        )
                    }
                }

                else -> {
                    _uiState.update { it.copy(isLoadingEvents = false) }
                }
            }
        }
    }

    private fun loadPaintings(forceRefresh: Boolean = false) {
        if (!forceRefresh && _uiState.value.eventPaintings.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingPaintings = true,
                    errorMessage = null
                )
            }

            when (val result = getPaintingsUseCase()) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingPaintings = false,
                            eventPaintings = result.data
                        )
                    }
                }

                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingPaintings = false,
                            errorMessage = result.error
                        )
                    }
                }

                else -> {
                    _uiState.update { it.copy(isLoadingPaintings = false) }
                }
            }
        }
    }

    fun refreshHome() {
        loadEvents(forceRefresh = true)
        loadPaintings(forceRefresh = true)
    }
}
