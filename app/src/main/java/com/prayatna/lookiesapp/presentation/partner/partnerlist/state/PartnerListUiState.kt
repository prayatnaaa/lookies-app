package com.prayatna.lookiesapp.presentation.partner.partnerlist.state

data class PartnerListUiState(
    val selectedType: MerchantBusinessType? = null,
    val selectedStatus: PartnerStatus? = null,
    val title: String = ""
)

enum class PartnerStatus(val value: String) {
    PENDING("not_started"),
    APPROVED("approved"),
    REJECTED("rejected")
}
