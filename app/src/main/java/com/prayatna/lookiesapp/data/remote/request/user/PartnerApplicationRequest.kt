package com.prayatna.lookiesapp.data.remote.request.user

import kotlinx.serialization.Serializable

@Serializable
data class PartnerApplicationRequest (
    val locName: String,
    val locUrl: String,
    val partnerName: String,
    val partnerType: String,
    val partnerLogo: ByteArray,
    val partnerPortfolioLink: String,
    val ktpFile: ByteArray,
    val businessLicenseFile: ByteArray,
    val bankName: String,
    val bankAccountNumber: String,
    val bankAccountHolder: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PartnerApplicationRequest

        if (locName != other.locName) return false
        if (locUrl != other.locUrl) return false
        if (partnerName != other.partnerName) return false
        if (partnerType != other.partnerType) return false
        if (!partnerLogo.contentEquals(other.partnerLogo)) return false
        if (partnerPortfolioLink != other.partnerPortfolioLink) return false
        if (!ktpFile.contentEquals(other.ktpFile)) return false
        if (!businessLicenseFile.contentEquals(other.businessLicenseFile)) return false
        if (bankName != other.bankName) return false
        if (bankAccountNumber != other.bankAccountNumber) return false
        if (bankAccountHolder != other.bankAccountHolder) return false

        return true
    }

    override fun hashCode(): Int {
        var result = locName.hashCode()
        result = 31 * result + locUrl.hashCode()
        result = 31 * result + partnerName.hashCode()
        result = 31 * result + partnerType.hashCode()
        result = 31 * result + partnerLogo.contentHashCode()
        result = 31 * result + partnerPortfolioLink.hashCode()
        result = 31 * result + ktpFile.contentHashCode()
        result = 31 * result + businessLicenseFile.contentHashCode()
        result = 31 * result + bankName.hashCode()
        result = 31 * result + bankAccountNumber.hashCode()
        result = 31 * result + bankAccountHolder.hashCode()
        return result
    }
}
