package com.papasmrufie.courseproject.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papasmrufie.courseproject.data.dao.TestsDao
import com.papasmrufie.courseproject.data.entity.SubjectEntity
import com.papasmrufie.courseproject.data.entity.TestEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.Int
import kotlin.String

class TestsViewModel(private val testsDao: TestsDao) : ViewModel() {

    private val _allTests = MutableStateFlow<List<TestEntity>>(emptyList())

    val allTests: StateFlow<List<TestEntity>> = _allTests

    init{
        viewModelScope.launch{
            testsDao.getAllTests().collect{
                    test -> _allTests.value = test
            }
        }
    }

    fun insertTest(title: String, desciption: String, subj_id: Int, dateTakePlace: Long){
        val testToInsert = TestEntity(
            testTitle = title,
            testDescription = desciption,
            subjectId = subj_id,
            dateTakePlace = dateTakePlace
        )
        viewModelScope.launch{
            testsDao.upsertTest(testToInsert)
        }
    }
    fun deleteTest(testEntity: TestEntity){
        viewModelScope.launch{
            testsDao.deleteTest(testEntity)
        }
    }
    fun updateTest(testEntity: TestEntity, newTitle: String, newDesciption: String, newSubj_id: Int, newDateTakePlace: Long){
        viewModelScope.launch{
            testsDao.upsertTest(testEntity.copy(testTitle = newTitle, testDescription = newDesciption, subjectId = newSubj_id, dateTakePlace = newDateTakePlace))
        }
    }

}