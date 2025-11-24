package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.domain.model.user.Profile
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.ProfileUiModel

fun Profile.toUi(): ProfileUiModel {
    return ProfileUiModel(
        id = this.id,
        profileUrl = this.profileUrl,
        username = this.username,
        fullName = this.fullName,
        address = this.address,
        bio = this.bio,
        hasPartnerSub = this.hasPartnerSub
    )
}