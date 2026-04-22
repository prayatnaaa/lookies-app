package com.prayatna.lookiesapp.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.prayatna.lookiesapp.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FcmTokenWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val userRepository: UserRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val token = inputData.getString("fcm_token")
                ?: return Result.failure()

            userRepository.updateFcmToken(token)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}