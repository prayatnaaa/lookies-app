package com.prayatna.lookiesapp.presentation.user.partnerSubmission.state
import android.net.Uri

sealed interface PartnerSubmissionEvent {
    data class LegalNameChanged(val value: String) : PartnerSubmissionEvent
    data class TradingNameChanged(val value: String) : PartnerSubmissionEvent
    data class DescriptionChanged(val value: String) : PartnerSubmissionEvent

    data class AddressChanged(val value: String) : PartnerSubmissionEvent
    data class CityChanged(val value: String) : PartnerSubmissionEvent
    data class ProvinceChanged(val value: String) : PartnerSubmissionEvent
    data class PostalCodeChanged(val value: String) : PartnerSubmissionEvent

    data class OwnerFirstNameChanged(val value: String) : PartnerSubmissionEvent
    data class OwnerLastNameChanged(val value: String) : PartnerSubmissionEvent
    data class OwnerEmailChanged(val value: String) : PartnerSubmissionEvent
    data class OwnerPhoneChanged(val value: String) : PartnerSubmissionEvent

    data class UseLoginEmailChanged(val isChecked: Boolean) : PartnerSubmissionEvent
    data class BusinessEmailChanged(val value: String) : PartnerSubmissionEvent

    data class BankCodeChanged(val value: String) : PartnerSubmissionEvent
    data class BankNameChanged(val value: String) : PartnerSubmissionEvent
    data class AccountNumberChanged(val value: String) : PartnerSubmissionEvent
    data class AccountHolderNameChanged(val value: String) : PartnerSubmissionEvent

    data class KycFileSelected(val uri: Uri) : PartnerSubmissionEvent

    data object Submit : PartnerSubmissionEvent
    data object DismissError : PartnerSubmissionEvent
}