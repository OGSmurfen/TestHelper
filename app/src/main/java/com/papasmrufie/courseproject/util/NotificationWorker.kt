package com.papasmrufie.courseproject.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    var c = context

    override fun doWork(): Result {
        // Perform background task here
        val notificationHandler = NotificationHandler(c)
        notificationHandler.showSimpleNotification()


        return Result.success()
    }


}