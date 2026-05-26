package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.domain.model.partner.DetailPartner
import com.prayatna.lookiesapp.domain.model.partner.GetLocation
import com.prayatna.lookiesapp.domain.model.partner.UserBank
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.DetailPartnerUiModel
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.GetLocationUi
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.UserBankUi

fun DetailPartner.toUi(): DetailPartnerUiModel {
    return DetailPartnerUiModel(
        name = this.name,
        type = this.type,
        logoUrl = this.logoUrl,
        locationId = this.locationId,
        portofolioLink = this.portofolioLink,
        status = this.status,
        createdAt = this.createdAt,
        ktpOwnerUrl = this.ktpOwnerUrl,
        businessLicenseUrl = this.businessLicenseUrl,
        userId = this.userId,
        id = this.id,
        locations = this.locations.map { it.toUi() },
        userBanks = this.userBanks.map { it.toUi() }
    )
}

private fun GetLocation.toUi(): GetLocationUi {
    return GetLocationUi(
        id = this.id,
        url = this.url,
        name = this.name,
        createdAt = this.createdAt,
        partnerId = this.partnerId
    )
}

private fun UserBank.toUi(): UserBankUi {
    return UserBankUi(
        id = this.id,
        bankName = this.bankName,
        partnerId = this.partnerId,
        bankAccountHolder = this.bankAccountHolder,
        bankAccountNumber = this.bankAccountNumber
    )
}