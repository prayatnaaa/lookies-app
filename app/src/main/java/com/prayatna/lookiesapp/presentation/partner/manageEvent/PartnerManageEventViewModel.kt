package com.prayatna.lookiesapp.presentation.partner.manageEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.domain.usecase.event.GetDetailEventUseCase
import com.prayatna.lookiesapp.presentation.partner.manageEvent.state.PartnerManageEventUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerManageEventViewModel @Inject constructor(
    private val getDetailEventUseCase: GetDetailEventUseCase,
    private val eventRepository: EventRepository
): ViewModel() {

    private val _state = MutableStateFlow(PartnerManageEventUiState())
    val state = _state.asStateFlow()

     fun getEvent(eventId: String) {
         _state.update { it.copy(isLoading = true) }
         viewModelScope.launch {
             when(val result = getDetailEventUseCase(eventId = eventId).first()) {
                 is DataResult.Error -> {
                     _state.update {
                         it.copy(
                             errorMessage = result.error,
                             isLoading = false,
                             event = null
                         )
                     }
                 }
                 is DataResult.Success -> {
                     _state.update {
                         it.copy(
                             event = result.data,
                             isLoading = false,
                             errorMessage = null
                         )
                     }
                 }
                 else -> Unit
             }
         }
     }

    fun getEventStatistic(eventId: String) {
        viewModelScope.launch {
            val result = eventRepository.getEventStatistics(eventId)
            result.collect { data ->
                when(data) {
                    is DataResult.Error -> {
                        _state.update {
                            it.copy(
                                errorMessage = data.error,
                                isLoading = false
                            )
                        }
                    }
                    is DataResult.Success-> {
                        _state.update {
                            it.copy(
                                statistics = data.data,
                                isLoading = false
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}