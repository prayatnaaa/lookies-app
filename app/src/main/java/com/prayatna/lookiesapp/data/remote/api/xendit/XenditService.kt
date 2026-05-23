package com.prayatna.lookiesapp.data.remote.api.xendit

import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.PayoutChannelDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import javax.inject.Inject

class XenditService @Inject constructor(
    private val httpClient: HttpClient
) {

    suspend fun getPayoutChannels(): List<PayoutChannelDto> {
        return httpClient.get(
            "https://api.xendit.co/payouts_channels"
        ) {
            basicAuth(
                username = BuildConfig.XENDIT_SECRET_KEY,
                password = ""
            )
        }.body()
    }
}