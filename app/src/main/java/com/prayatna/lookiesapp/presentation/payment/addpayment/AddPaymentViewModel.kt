package com.prayatna.lookiesapp.presentation.payment.addpayment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.payment.AddPayment
import com.prayatna.lookiesapp.domain.usecase.payment.AddPaymentUseCase
import com.prayatna.lookiesapp.presentation.payment.addpayment.state.AddPaymentFormState
import com.prayatna.lookiesapp.presentation.payment.addpayment.state.AddPaymentUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPaymentViewModel @Inject constructor(
    private val addPaymentUseCase: AddPaymentUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _addPaymentState = MutableStateFlow(AddPaymentUiState())
    val addPaymentState: StateFlow<AddPaymentUiState> = _addPaymentState

    private val _addPaymentFormState = MutableStateFlow(AddPaymentFormState())
    val addPaymentFormState: StateFlow<AddPaymentFormState> = _addPaymentFormState

    fun onAmountChanged(value: String) {
        _addPaymentFormState.update { it.copy(amount = value) }
    }

    fun onPaymentTypeChanged(value: String) {
        _addPaymentFormState.update { it.copy( paymentType = value) }
    }

    init {
        val quantity = savedStateHandle.get<Int>("quantity") ?: 0
        Log.d("PAYMENT", quantity.toString())
        _addPaymentFormState.update { it.copy(quantity = quantity.toString()) }
    }

    fun submitPayment() {
        val form = _addPaymentFormState.value
        viewModelScope.launch {
            _addPaymentState.update { it.copy(isLoading = true, error = null) }
            val result = addPaymentUseCase(AddPayment(
                amount = form.amount.toDouble(),
                paymentType = form.paymentType,
                quantity = form.quantity.toInt(),
                userId = ""
            ))

            _addPaymentState.update {
                when (result) {
                    is DataResult.Success -> it.copy(
                        isLoading = false,
                        success = result.data,
                        error = null
                    )
                    is DataResult.Error -> it.copy(
                        isLoading = false,
                        error = result.error
                    )

                    DataResult.Idle -> {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                    DataResult.Loading -> {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                }
            }
        }
    }
}
