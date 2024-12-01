package com.papasmrufie.courseproject.data.database

import android.content.Context
import androidx.room.Room

object AppDatabaseProvider {

    @Volatile
    private var instance: AppDb? = null

    fun getDatabase(context: Context): AppDb {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDb::class.java,
                "app_db"
            ).build().also { instance = it }
        }
    }
}