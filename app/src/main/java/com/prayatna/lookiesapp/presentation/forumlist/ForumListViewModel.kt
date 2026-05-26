package com.prayatna.lookiesapp.presentation.forumlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.chat.GetForumsUseCase
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumListViewModel @Inject constructor(
    private val getForumsUseCase: GetForumsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForumListUiState())
    val uiState: StateFlow<ForumListUiState> = _uiState.asStateFlow()

    init {
        loadForums()
    }

    fun onEvent(event: ForumListEvent) {
        when (event) {
            is ForumListEvent.Refresh -> {
                loadForums()
            }
            is ForumListEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            is ForumListEvent.OnForumClick -> {
                // Handled in UI
            }
        }
    }

    private fun loadForums() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getForumsUseCase()) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            forums = result.data
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
