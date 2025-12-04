package com.prayatna.lookiesapp.domain.model.partner

import kotlinx.datetime.Instant

data class DetailPartner(
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
    val locations: List<GetLocation> = emptyList(),
    val userBanks: List<UserBank> = emptyList()
)

data class GetLocation(
    val id: Int,
    val url: String?,
    val name: String,
    val createdAt: Instant,
    val partnerId: String
)

data class UserBank(
    val id: String,
    val bankName: String,
    val partnerId: String,
    val bankAccountHolder: String,
    val bankAccountNumber: String
)
