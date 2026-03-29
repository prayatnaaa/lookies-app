package com.prayatna.lookiesapp.presentation.transaction.detailTransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.transaction.GetDetailTransactionUseCase
import com.prayatna.lookiesapp.presentation.transaction.detailTransaction.state.DetailTransactionUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTransactionViewModel @Inject constructor(
    private val getDetailTransactionUseCase: GetDetailTransactionUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(DetailTransactionUiState())
    val uiState: StateFlow<DetailTransactionUiState> = _uiState.asStateFlow()

    fun getDetailTransaction(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when(val result = getDetailTransactionUseCase(orderId)) {
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.error,
                            isLoading = false
                        )
                    }
                }
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = null,
                            isLoading = false,
                            data = result.data
                        )
                    }
                }
                else -> Unit
            }

        }
    }

}