package com.papasmrufie.courseproject.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.papasmrufie.courseproject.data.entity.DifficultyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DifficultiesDao {
    @Upsert
    suspend fun upsertDifficulty(e: DifficultyEntity)


    @Query("SELECT * FROM difficulties ORDER BY difficultyId ASC")
    fun getAllDifficulties(): Flow<List<DifficultyEntity>>
}