package com.prayatna.lookiesapp.presentation.partner.detailpartner.state

sealed interface DetailPartnerEffect {
    data class ViewPrivateFile(val url: String) : DetailPartnerEffect
    data class ShowError(val message: String) : DetailPartnerEffect
}
