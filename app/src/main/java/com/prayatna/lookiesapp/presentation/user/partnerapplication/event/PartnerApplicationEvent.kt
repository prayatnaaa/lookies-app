package com.prayatna.lookiesapp.presentation.user.partnerapplication.event

import android.net.Uri

sealed class PartnerApplicationEvent {

    data class LocNameChanged(val value: String) : PartnerApplicationEvent()
    data class LocUrlChanged(val value: String) : PartnerApplicationEvent()

    data class PartnerNameChanged(val value: String) : PartnerApplicationEvent()
    data class PartnerTypeChanged(val value: String) : PartnerApplicationEvent()

    data class PartnerLogoChanged(val value: Uri) : PartnerApplicationEvent()
    data class PortfolioLinkChanged(val value: String) : PartnerApplicationEvent()

    data class KtpFileChanged(val value: Uri) : PartnerApplicationEvent()
    data class BusinessLicenseFileChanged(val value: Uri) : PartnerApplicationEvent()

    data class BankNameChanged(val value: String) : PartnerApplicationEvent()
    data class BankAccountNumberChanged(val value: String) : PartnerApplicationEvent()
    data class BankAccountHolderChanged(val value: String) : PartnerApplicationEvent()

    data object Submit : PartnerApplicationEvent()
}

