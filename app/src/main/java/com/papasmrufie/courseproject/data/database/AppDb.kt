package com.papasmrufie.courseproject.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.papasmrufie.courseproject.data.dao.DifficultiesDao
import com.papasmrufie.courseproject.data.dao.SubjectsDao
import com.papasmrufie.courseproject.data.dao.TestsDao
import com.papasmrufie.courseproject.data.entity.DifficultyEntity
import com.papasmrufie.courseproject.data.entity.SubjectEntity
import com.papasmrufie.courseproject.data.entity.TestEntity

@Database(
    entities = [SubjectEntity::class, TestEntity::class, DifficultyEntity::class],
    version = 1
)

abstract class AppDb : RoomDatabase() {
    abstract val subjectsDao: SubjectsDao
    abstract val testsDao: TestsDao
    abstract val difficultiesDao: DifficultiesDao
}