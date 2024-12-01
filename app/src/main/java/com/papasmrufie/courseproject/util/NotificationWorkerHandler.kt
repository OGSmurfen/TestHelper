package com.papasmrufie.courseproject.util

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class NotificationWorkerHandler(private val context: Context) {

    fun setupWorkDelayed(millis: Long){
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(millis, TimeUnit.MILLISECONDS)
            .setConstraints(Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .build())
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}