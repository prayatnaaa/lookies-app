package com.prayatna.lookiesapp.presentation.admin.detailEvent.state

sealed interface AdminDetailEventUiEffect {
    data class ShowToast(val message: String) : AdminDetailEventUiEffect
    data object NavigateBack : AdminDetailEventUiEffect
}
