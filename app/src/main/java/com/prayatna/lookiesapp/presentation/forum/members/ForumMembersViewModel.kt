package com.prayatna.lookiesapp.presentation.forum.members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.chat.GetForumMembersUseCase
import com.prayatna.lookiesapp.presentation.forum.members.state.ForumMembersUiEffect
import com.prayatna.lookiesapp.presentation.forum.members.state.ForumMembersUiEvent
import com.prayatna.lookiesapp.presentation.forum.members.state.ForumMembersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumMembersViewModel @Inject constructor(
    private val getForumMembersUseCase: GetForumMembersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForumMembersUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ForumMembersUiEffect>()
    val effect = _effect.asSharedFlow()
    
    private var loadJob: Job? = null

    fun onEvent(event: ForumMembersUiEvent) {
        when (event) {
            is ForumMembersUiEvent.LoadMembers -> {
                if (_uiState.value.members.isEmpty()) {
                    loadMembers(event.forumId)
                }
            }
        }
    }

    private fun loadMembers(forumId: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getForumMembersUseCase(forumId)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
                .collectLatest { members ->
                    _uiState.update { it.copy(isLoading = false, members = members) }
                }
        }
    }
}
