package com.prayatna.lookiesapp.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey
    val id: Int,

    val title: String,

    @ColumnInfo(name = "organizer_id")
    val organizerId: String,

    @ColumnInfo(name = "organizer_type")
    val organizerType: String,

    @ColumnInfo(name = "organizer_legal_name")
    val organizerLegalName: String,

    @ColumnInfo(name = "organizer_trading_name")
    val organizerTradingName: String?,

    @ColumnInfo(name = "organizer_description")
    val organizerDescription: String?,

    @ColumnInfo(name = "organizer_industry_category")
    val organizerIndustryCategory: String,

    @ColumnInfo(name = "organizer_date_of_registration")
    val organizerDateOfRegistration: String?,

    @ColumnInfo(name = "organizer_country_of_operation")
    val organizerCountryOfOperation: String,

    @ColumnInfo(name = "organizer_website_url")
    val organizerWebsiteUrl: String?,

    @ColumnInfo(name = "organizer_phone_number")
    val organizerPhoneNumber: String?,

    @ColumnInfo(name = "organizer_email")
    val organizerEmail: String?,

    @ColumnInfo(name = "organizer_created_at")
    val organizerCreatedAt: String?,

    @ColumnInfo(name = "organizer_updated_at")
    val organizerUpdatedAt: String?,

    @ColumnInfo(name = "organizer_picture_url")
    val organizerPictureUrl: String?,

    @ColumnInfo(name = "organizer_merchant_type")
    val organizerMerchantType: String,

    @ColumnInfo(name = "organizer_status")
    val organizerStatus: String?,

    @ColumnInfo(name = "banner_image_url")
    val bannerImageUrl: String,

    @ColumnInfo(name = "start_date")
    val startDate: String,

    @ColumnInfo(name = "end_date")
    val endDate: String,

    val about: String? = null,

    val location: String,

    @ColumnInfo(name = "location_url")
    val locationUrl: String,

    @ColumnInfo(name = "max_participant")
    val maxParticipant: Int? = null,

    @ColumnInfo(name = "remaining_participant_quota")
    val remainingParticipantQuota: Int? = null,

    @ColumnInfo(name = "remaining_painting_quota")
    val remainingPaintingQuota: Int? = null,

    @ColumnInfo(name = "max_painting")
    val maxPainting: Int? = null,

    @ColumnInfo(name = "max_painting_per_artist")
    val maxPaintingPerArtist: Int? = null,

    @ColumnInfo(name = "ticket_price")
    val ticketPrice: Double? = null,

    @ColumnInfo(name = "artist_registration_fee")
    val artistRegistrationFee: Double? = null,

    @ColumnInfo(name = "event_type_id")
    val eventTypeId: Int,

    @ColumnInfo(name = "event_type_name")
    val eventTypeName: String,

    @ColumnInfo(name = "event_type_slug")
    val eventTypeSlug: String,

    @ColumnInfo(name = "event_format_id")
    val eventFormatId: Int,

    @ColumnInfo(name = "event_format_name")
    val eventFormatName: String,

    @ColumnInfo(name = "event_format_slug")
    val eventFormatSlug: String,

    val status: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "updated_at")
    val updatedAt: String? = null,

    @ColumnInfo(name = "rejection_reason")
    val rejectionReason: String? = null,

    @ColumnInfo(name = "approved_by")
    val approvedBy: String? = null,

    @ColumnInfo(name = "approved_at")
    val approvedAt: String? = null,

    @ColumnInfo(name = "painting_submission_deadline")
    val paintingSubmissionDeadline: String? = null,

    @ColumnInfo(name = "artist_registration_start_date")
    val artistRegistrationStartDate: String? = null,

    @ColumnInfo(name = "artist_registration_end_date")
    val artistRegistrationEndDate: String? = null
)