package com.papasmrufie.courseproject.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "difficulties")
data class DifficultyEntity(
    @PrimaryKey(autoGenerate = true)
    val difficultyId: Int = 0,
    val difficultyDescription: String
)