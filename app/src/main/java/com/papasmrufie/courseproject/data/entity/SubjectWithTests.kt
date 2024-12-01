package com.papasmrufie.courseproject.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SubjectWithTests (
    @Embedded val subject: SubjectEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "subjectId"
    )
    val tests: List<TestEntity>
)