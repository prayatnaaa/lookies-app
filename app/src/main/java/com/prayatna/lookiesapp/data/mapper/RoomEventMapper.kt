package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.EventDto
import com.prayatna.lookiesapp.data.local.room.entity.Event as EventEntity
import com.prayatna.lookiesapp.domain.model.event.Event as DomainEvent
import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.TEventType
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness

fun EventDto.toEntity() = EventEntity(
    id = eventId.toInt(),
    title = title,
    organizerId = organizer.id,
    organizerType = organizer.type,
    organizerLegalName = organizer.legalName,
    organizerTradingName = organizer.tradingName,
    organizerDescription = organizer.description,
    organizerIndustryCategory = organizer.industryCategory,
    organizerDateOfRegistration = organizer.dateOfRegistration,
    organizerCountryOfOperation = organizer.countryOfOperation,
    organizerWebsiteUrl = organizer.websiteUrl,
    organizerPhoneNumber = organizer.phoneNumber,
    organizerEmail = organizer.email,
    organizerCreatedAt = organizer.createdAt,
    organizerUpdatedAt = organizer.updatedAt,
    organizerPictureUrl = organizer.pictureUrl,
    organizerMerchantType = organizer.merchantType,
    organizerStatus = organizer.status,
    bannerImageUrl = bannerImageUrl,
    startDate = startDate,
    endDate = endDate,
    about = about,
    location = location,
    locationUrl = locationUrl,
    maxParticipant = maxParticipant,
    remainingParticipantQuota = remainingParticipantQuota,
    remainingPaintingQuota = remainingPaintingQuota,
    maxPainting = maxPainting,
    maxPaintingPerArtist = maxPaintingPerArtist,
    ticketPrice = ticketPrice,
    artistRegistrationFee = registrationFee,
    eventTypeId = eventType.id,
    eventTypeName = eventType.name,
    eventTypeSlug = eventType.slug,
    eventFormatId = eventFormat.id,
    eventFormatName = eventFormat.name,
    eventFormatSlug = eventFormat.slug,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
    rejectionReason = rejectionReason,
    approvedBy = approvedBy,
    approvedAt = approvedAt,
    paintingSubmissionDeadline = paintingSubmissionDeadline,
    artistRegistrationStartDate = artistRegistrationStartDate,
    artistRegistrationEndDate = artistRegistrationEndDate
)

fun EventEntity.toDomain() = DomainEvent(
    id = id.toString(),
    title = title,
    bannerImageUrl = bannerImageUrl,
    startDate = startDate,
    endDate = endDate,
    about = about,
    location = location,
    locationUrl = locationUrl,
    maxParticipant = maxParticipant,
    remainingParticipantQuota = remainingParticipantQuota,
    remainingPaintingQuota = remainingPaintingQuota,
    maxPainting = maxPainting,
    maxPaintingPerArtist = maxPaintingPerArtist,
    ticketPrice = ticketPrice,
    artistRegistrationFee = artistRegistrationFee,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
    rejectionReason = rejectionReason,
    approvedAt = approvedAt,
    approvedBy = approvedBy,
    paintingSubmissionDeadline = paintingSubmissionDeadline,
    artistRegistrationEndDate = artistRegistrationEndDate,
    artistRegistrationStartDate = artistRegistrationStartDate,
    eventType = TEventType(
        id = eventTypeId,
        name = eventTypeName,
        slug = eventTypeSlug
    ),
    eventFormat = EventFormat(
        id = eventFormatId,
        name = eventFormatName,
        slug = eventFormatSlug
    ),
    organizer = MerchantBusiness(
        id = organizerId,
        type = organizerType,
        legalName = organizerLegalName,
        tradingName = organizerTradingName,
        description = organizerDescription,
        industryCategory = organizerIndustryCategory,
        dateOfRegistration = organizerDateOfRegistration,
        countryOfOperation = organizerCountryOfOperation,
        websiteUrl = organizerWebsiteUrl,
        phoneNumber = organizerPhoneNumber,
        email = organizerEmail,
        createdAt = organizerCreatedAt.orEmpty(),
        updatedAt = organizerUpdatedAt,
        merchantType = organizerMerchantType,
        pictureUrl = organizerPictureUrl,
        status = organizerStatus
    )
)
