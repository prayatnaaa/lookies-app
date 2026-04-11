package com.prayatna.lookiesapp.presentation.forum.forumchannellist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val getForumChannelsUseCase: GetForumChannelsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForumChannelListUiState())
    val uiState: StateFlow<ForumChannelListUiState> = _uiState.asStateFlow()

    fun initForum(forumId: String) {
        if (_uiState.value.forumId == forumId && _uiState.value.channels.isNotEmpty()) return
        
        _uiState.update { it.copy(forumId = forumId) }
        loadChannels()
    }

    fun onEvent(event: ForumChannelListEvent) {
        when (event) {
            is ForumChannelListEvent.Refresh -> {
                loadChannels()
            }
            is ForumChannelListEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            is ForumChannelListEvent.OnChannelClick -> {
                // Handled in UI
            }
        }
    }

    private fun loadChannels() {
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
                else -> Unit
            }
        }
    }
}
