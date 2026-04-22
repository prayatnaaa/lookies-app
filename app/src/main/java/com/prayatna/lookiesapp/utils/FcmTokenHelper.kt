package com.prayatna.lookiesapp.utils

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.prayatna.lookiesapp.worker.FcmTokenWorker
import java.util.concurrent.TimeUnit

fun enqueueFcmWorker(context: Context, token: String) {
    val data = workDataOf("fcm_token" to token)

    val request = OneTimeWorkRequestBuilder<FcmTokenWorker>()
        .setInputData(data)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            10_000,
            TimeUnit.MILLISECONDS
        )
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "fcm_token_sync",
        ExistingWorkPolicy.KEEP,
        request
    )
}