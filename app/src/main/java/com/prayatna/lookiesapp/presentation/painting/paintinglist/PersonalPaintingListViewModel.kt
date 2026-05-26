package com.prayatna.lookiesapp.presentation.painting.paintinglist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.painting.PaintingFilter
import com.prayatna.lookiesapp.domain.model.painting.SortType
import com.prayatna.lookiesapp.domain.usecase.painting.GetArtistPaintingsUseCase
import com.prayatna.lookiesapp.presentation.painting.paintinglist.state.PaintingUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalPaintingListViewModel @Inject constructor(
    private val getPaintingsUseCase: GetArtistPaintingsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    val refresh = savedStateHandle.getStateFlow(
        key = "refresh",
        initialValue = false
    )
    private val _uiState = MutableStateFlow(PaintingUiState())
    val uiState: StateFlow<PaintingUiState> = _uiState.asStateFlow()

    private var currentFilter = PaintingFilter()
    private var artistId: String? = null

    init {
        observeRefresh()
    }

    fun init(artistId: String) {
        if (this.artistId == null) {
            this.artistId = artistId
            loadPaintings()
        }
    }

    private fun loadPaintings(filter: PaintingFilter = currentFilter) {
        val id = artistId ?: return

        currentFilter = filter

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getPaintingsUseCase(filter = filter, id = id)) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            paintings = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }

                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.error,
                            isLoading = false
                        )
                    }
                }
                else -> {}
            }
        }
    }

    private fun observeRefresh() {
        viewModelScope.launch {
            refresh.collect { shouldRefresh ->
                if (shouldRefresh) {
                    loadPaintings()

                    savedStateHandle["refresh"] = false
                }
            }
        }
    }

    fun applyFilter(
        medium: String? = null,
        subject: String? = null,
        artStyle: String? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        sort: SortType? = null
    ) {
        val updatedFilter = currentFilter.copy(
            medium = medium ?: currentFilter.medium,
            subject = subject ?: currentFilter.subject,
            artStyle = artStyle ?: currentFilter.artStyle,
            minYear = minYear ?: currentFilter.minYear,
            maxYear = maxYear ?: currentFilter.maxYear,
            sortBy = sort ?: currentFilter.sortBy
        )
        loadPaintings(updatedFilter)
    }
}
