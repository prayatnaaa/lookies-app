package com.prayatna.lookiesapp.presentation.partner.detailpartner.state

import kotlinx.datetime.Instant

data class DetailPartnerUiModel(
    val name: String,
    val type: String,
    val logoUrl: String?,
    val locationId: String?,
    val portofolioLink: String?,
    val status: String,
    val createdAt: Instant,
    val ktpOwnerUrl: String?,
    val businessLicenseUrl: String?,
    val userId: String,
    val id: String,
    val locations: List<GetLocationUi> = emptyList(),
    val userBanks: List<UserBankUi> = emptyList()
)

data class GetLocationUi(
    val id: Int,
    val url: String?,
    val name: String,
    val createdAt: Instant,
    val partnerId: String
)

data class UserBankUi(
    val id: String,
    val bankName: String,
    val partnerId: String,
    val bankAccountHolder: String,
    val bankAccountNumber: String
)
