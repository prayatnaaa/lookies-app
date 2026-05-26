package com.prayatna.lookiesapp.presentation.user.artistSubmission.state

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.user.Gender

sealed class ArtistSubmissionEvent {
    data class FullNameChanged(val value: String) : ArtistSubmissionEvent()
    data class DisplayNameChanged(val value: String) : ArtistSubmissionEvent()
    data class BioChanged(val value: String) : ArtistSubmissionEvent()
    data class PhoneNumberChanged(val value: String) : ArtistSubmissionEvent()
    data class NationalityChanged(val value: String) : ArtistSubmissionEvent()
    data class PlaceOfBirthChanged(val value: String) : ArtistSubmissionEvent()
    data class DateOfBirthChanged(val value: String) : ArtistSubmissionEvent()
    data class GenderChanged(val value: Gender?) : ArtistSubmissionEvent()
    data class WebsiteChanged(val value: String) : ArtistSubmissionEvent()
    data class CountryChanged(val value: String) : ArtistSubmissionEvent()
    
    data class AddressChanged(val value: String) : ArtistSubmissionEvent()
    data class AddressLine2Changed(val value: String) : ArtistSubmissionEvent()
    data class CityChanged(val value: String) : ArtistSubmissionEvent()
    data class ProvinceChanged(val value: String) : ArtistSubmissionEvent()
    data class DistrictChanged(val value: String) : ArtistSubmissionEvent()
    data class SubDistrictChanged(val value: String) : ArtistSubmissionEvent()
    data class PostalCodeChanged(val value: String) : ArtistSubmissionEvent()
    
    data class BankCodeChanged(val value: String) : ArtistSubmissionEvent()
    data class BankNameChanged(val value: String) : ArtistSubmissionEvent()
    data class AccountNumberChanged(val value: String) : ArtistSubmissionEvent()
    data class AccountHolderNameChanged(val value: String) : ArtistSubmissionEvent()
    
    data class KycFileSelected(val uri: Uri) : ArtistSubmissionEvent()
    
    data object Submit : ArtistSubmissionEvent()
    data object DismissError : ArtistSubmissionEvent()
    data object OnBack : ArtistSubmissionEvent()
}
