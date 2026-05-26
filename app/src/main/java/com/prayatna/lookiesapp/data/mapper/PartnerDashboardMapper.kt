package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.PartnerDashboardDto
import com.prayatna.lookiesapp.domain.model.partner.PartnerDashboard

fun PartnerDashboardDto.toDomain(): PartnerDashboard {
    return PartnerDashboard(
        partnerId = partnerId,
        partnerName = partnerName,
        totalEventsCreated = totalEventsCreated,
        activeEvents = activeEvents,
        totalTicketsSold = totalTicketsSold,
        totalRevenue = totalRevenue,
        pendingPayout = pendingPayout,
    )
}