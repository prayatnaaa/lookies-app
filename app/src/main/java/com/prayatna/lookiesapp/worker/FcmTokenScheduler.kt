package com.prayatna.lookiesapp.worker

import android.content.Context
import com.prayatna.lookiesapp.utils.enqueueFcmWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FcmTokenScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun enqueue(token: String) {
        enqueueFcmWorker(context, token)
    }
}