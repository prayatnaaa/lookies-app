package com.prayatna.lookiesapp.presentation.forumchannellist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.chat.CreateForumChannelUseCase
import com.prayatna.lookiesapp.domain.usecase.chat.GetForumChannelsUseCase
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumChannelListViewModel @Inject constructor(
    private val getForumChannelsUseCase: GetForumChannelsUseCase,
    private val createForumChannelUseCase: CreateForumChannelUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userRole = savedStateHandle.get<String?>("role")

    private val _uiState = MutableStateFlow(ForumChannelListUiState())
    val uiState: StateFlow<ForumChannelListUiState> = _uiState.asStateFlow()

    init {
        userRole?.let { role ->
            _uiState.update { it.copy(userRole = role) }
        }
    }


    fun initForum(forumId: String) {
        if (_uiState.value.forumId == forumId && _uiState.value.channels.isNotEmpty()) return
        
        _uiState.update { it.copy(forumId = forumId) }
        loadData()
    }

    fun onEvent(event: ForumChannelListEvent) {
        when (event) {
            is ForumChannelListEvent.Refresh -> {
                loadData()
            }
            is ForumChannelListEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            is ForumChannelListEvent.OnChannelClick -> {
                // Handled in UI
            }
            is ForumChannelListEvent.CreateChannel -> {
                createChannel(event.name, event.isReadOnly)
            }
        }
    }

    private fun loadData() {
        val forumId = _uiState.value.forumId
        if (forumId.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getForumChannelsUseCase(forumId)) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            channels = result.data
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun createChannel(name: String, isReadOnly: Boolean) {
        val forumId = _uiState.value.forumId
        if (forumId.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingChannel = true) }
            when (val result = createForumChannelUseCase(forumId, name, isReadOnly)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isCreatingChannel = false) }
                    loadData()
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isCreatingChannel = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isCreatingChannel = false) }
            }
        }
    }
}
