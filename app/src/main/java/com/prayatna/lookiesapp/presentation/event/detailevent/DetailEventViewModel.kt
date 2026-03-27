package com.prayatna.lookiesapp.presentation.event.detailevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.domain.usecase.admin.ApproveEventUseCase
import com.prayatna.lookiesapp.domain.usecase.admin.RejectEventUseCase
import com.prayatna.lookiesapp.domain.usecase.auth.GetRoleUseCase
import com.prayatna.lookiesapp.domain.usecase.painting.GetPaintingUseCase
import com.prayatna.lookiesapp.presentation.event.detailevent.state.DecideEventState
import com.prayatna.lookiesapp.presentation.event.detailevent.state.DetailEventUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailEventViewModel @Inject constructor(
    private val repository: EventRepository,
    private val getRoleUseCase: GetRoleUseCase,
    private val getPaintingUseCase: GetPaintingUseCase,
    private val approveEventUseCase: ApproveEventUseCase,
    private val rejectEventUseCase: RejectEventUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailEventUiState())
    val state = _state.asStateFlow()

    private val _adminState = MutableStateFlow(DecideEventState())
    val adminState: StateFlow<DecideEventState> = _adminState.asStateFlow()


    private val _quantityValue = MutableStateFlow(0)
    val quantityValue = _quantityValue.asStateFlow()

    private val _roleState = MutableStateFlow("")
    val roleState = _roleState.asStateFlow()


    init {
        viewModelScope.launch {
            getRoleUseCase().collect { role ->
                _roleState.value = role
            }
        }
    }

    fun setQuantityValue(value: Int) {
        _quantityValue.value = value
    }

    fun getEventPaintings(eventId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, eventPaintingsError = null) }

            when (
                val result = getPaintingUseCase(
                    eventId = eventId,
                    status = "accepted",
                    showSelfPaintings = true
                )
            ) {
                is DataResult.Success -> {
                    _state.update {
                        it.copy(
                            paintings = result.data,
                            isLoading = false
                        )
                    }
                }

                is DataResult.Error -> {
                    _state.update {
                        it.copy(
                            eventPaintingsError = result.error,
                            isLoading = false
                        )
                    }
                }

                is DataResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }

                else -> Unit
            }
        }
    }



    fun getEvent(eventId: String, forceRefresh: Boolean = false) {
        if (!forceRefresh && _state.value.info != null) return

        viewModelScope.launch {
            _state.value = DetailEventUiState(isLoading = true)

            when (val result = repository.getEvent(eventId)) {
                is DataResult.Error -> {
                    _state.update {
                        it.copy(
                            detailEventError = result.error,
                            isLoading = false
                        )
                    }
                }
                is DataResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
                is DataResult.Success -> {
                   _state.update {
                       it.copy(
                           info = result.data,
                           isLoading = false,
                           detailEventError = null
                       )
                   }
                }
                else -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun approveEvent(eventId: String) {
        viewModelScope.launch {
            _adminState.update { it.copy(isLoading = true, error = null, success = null) }

            when (val result = approveEventUseCase(eventId.toInt())) {
                is DataResult.Success -> {
                    _adminState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            success = result.data,
                        )
                    }
                    getEvent(eventId, forceRefresh = true)
                }

                is DataResult.Error -> {
                    _adminState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                }

                is DataResult.Loading -> {
                    _adminState.update { it.copy(isLoading = true) }
                }

                else -> Unit
            }
        }
    }

    fun rejectEvent(eventId: String) {
        viewModelScope.launch {
            _adminState.update { it.copy(isLoading = true, error = null, success = null) }

            when (val result = rejectEventUseCase(eventId.toInt())) {
                is DataResult.Success -> {
                    _adminState.update {
                        it.copy(
                            isLoading = false,
                            success = result.data,
                        )
                    }

                    getEvent(eventId, forceRefresh = true)
                }

                is DataResult.Error -> {
                    _adminState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                }

                is DataResult.Loading -> {
                    _adminState.update { it.copy(isLoading = true) }
                }

                else -> Unit
            }
        }
    }

}
