package com.prayatna.lookiesapp.presentation.merchant.editMerchantProfile.state

sealed interface EditMerchantProfileEvent {
    data class Load(val businessId: String) : EditMerchantProfileEvent
    
    // Business Changes
    data class LegalNameChanged(val value: String) : EditMerchantProfileEvent
    data class TradingNameChanged(val value: String) : EditMerchantProfileEvent
    data class DescriptionChanged(val value: String) : EditMerchantProfileEvent
    data class IndustryCategoryChanged(val value: String) : EditMerchantProfileEvent
    data class PhoneNumberChanged(val value: String) : EditMerchantProfileEvent
    data class EmailChanged(val value: String) : EditMerchantProfileEvent
    data class WebsiteUrlChanged(val value: String) : EditMerchantProfileEvent
    data class PictureUrlChanged(val value: String) : EditMerchantProfileEvent
    
    // Bank Changes
    data class BankSelected(val code: String, val name: String) : EditMerchantProfileEvent
    data class AccountNumberChanged(val value: String) : EditMerchantProfileEvent
    data class AccountHolderNameChanged(val value: String) : EditMerchantProfileEvent
    
    data object Save : EditMerchantProfileEvent
    data object DismissMessage : EditMerchantProfileEvent
}
