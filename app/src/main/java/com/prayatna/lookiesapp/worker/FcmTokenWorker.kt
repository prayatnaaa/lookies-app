package com.prayatna.lookiesapp.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
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

            when (val result = userRepository.updateFcmToken(token)) {
                is DataResult.Success -> Result.success()
                is DataResult.Error -> {
                    Log.e("FcmTokenWorker", "Gagal update token: ${result.error}")
                    Result.retry()
                }
                else -> Result.failure()
            }
        } catch (e: Exception) {
            Log.e("FcmTokenWorker", "Exception di worker: ${e.message}")
            Result.retry()
        }
    }
}