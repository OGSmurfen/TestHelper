package com.papasmrufie.courseproject.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.papasmrufie.courseproject.data.entity.TestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TestsDao {
    @Upsert
    suspend fun upsertTest(testE: TestEntity)

    @Delete
    suspend fun deleteTest(testE: TestEntity)

    @Query("SELECT * FROM tests ORDER BY testId ASC")
    fun getAllTests(): Flow<List<TestEntity>>
}