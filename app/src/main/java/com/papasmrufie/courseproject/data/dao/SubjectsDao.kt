package com.papasmrufie.courseproject.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.papasmrufie.courseproject.data.entity.SubjectEntity
import com.papasmrufie.courseproject.data.entity.SubjectWithTests
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectsDao{
    @Upsert
    suspend fun upsertSubject(e: SubjectEntity)

    @Delete
    suspend fun deleteSubject(e: SubjectEntity)

    @Query("SELECT * FROM subjects ORDER BY id ASC")
    fun getAllSubjects(): Flow<List<SubjectEntity>>

    @Transaction
    @Query("SELECT * FROM subjects WHERE id = :subjectId")
    fun getSubjectWithTests(subjectId: Int): SubjectWithTests
}