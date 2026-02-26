package com.prayatna.lookiesapp.presentation.partner.detailpartner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.admin.ApprovePartnerUseCase
import com.prayatna.lookiesapp.domain.usecase.admin.RejectPartnerUseCase
import com.prayatna.lookiesapp.domain.usecase.auth.GetRoleUseCase
import com.prayatna.lookiesapp.domain.usecase.partner.GetDetailPartnerUseCase
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.AdminDecideState
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
    private val getDetailPartnerUseCase: GetDetailPartnerUseCase,
    private val getRolesUseCase: GetRoleUseCase,
    private val approvePartnerUseCase: ApprovePartnerUseCase,
    private val rejectPartnerUseCase: RejectPartnerUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailPartnerState())
    val state = _state.asStateFlow()

    private val _adminState = MutableStateFlow(AdminDecideState())
    val adminState = _adminState.asStateFlow()

    private val _roleState = MutableStateFlow("")
    val roleState = _roleState.asStateFlow()

    init {
        viewModelScope.launch {
            getRolesUseCase().collect { role ->
                _roleState.value = role
            }
        }
    }

    private fun decidePartner(
        partnerId: String,
        action: suspend (String) -> DataResult<String>
    ) {
        viewModelScope.launch {
            _adminState.update { it.copy(isLoading = true, error = null) }

            when (val result = action(partnerId)) {
                is DataResult.Success -> {
                    _adminState.update {
                        it.copy(
                            isLoading = false,
                            success = result.data,
                            error = null
                        )
                    }
                }

                is DataResult.Error -> {
                    _adminState.update {
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

    fun approvePartner(partnerId: String) {
        decidePartner(partnerId, approvePartnerUseCase::invoke)
    }

    fun rejectPartner(partnerId: String) {
        decidePartner(partnerId, rejectPartnerUseCase::invoke)
    }

    fun loadPartnerDetail(id: String) {
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
                                data = result.data,
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
