package com.papasmrufie.courseproject.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tests",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TestEntity (
    @PrimaryKey(autoGenerate = true)
    val testId: Int = 0,
    val testTitle: String,
    val testDescription: String,
    val subjectId: Int,
    val dateTakePlace: Long
)