package com.prayatna.lookiesapp.presentation.partner.detailpartner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.mapper.toUi
import com.prayatna.lookiesapp.domain.usecase.partner.GetDetailPartnerUseCase
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.DetailPartnerState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPartnerViewModel @Inject constructor(
    private val getDetailPartnerUseCase: GetDetailPartnerUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailPartnerState())
    val state = _state.asStateFlow()

    fun loadPartnerDetail(id: Int) {
        viewModelScope.launch {
            getDetailPartnerUseCase(id).collect { result ->
                when (result) {

                    is DataResult.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }

                    is DataResult.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                data = result.data.toUi(),
                                error = null
                            )
                        }
                    }

                    is DataResult.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.error
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}
