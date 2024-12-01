package com.papasmrufie.courseproject.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papasmrufie.courseproject.data.dao.SubjectsDao
import com.papasmrufie.courseproject.data.entity.SubjectEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubjectsViewModel(private val subjectsDao: SubjectsDao) : ViewModel() {
    private val _allSubjects = MutableStateFlow<List<SubjectEntity>>(emptyList())
    val allSubjects: StateFlow<List<SubjectEntity>> = _allSubjects

    init{
        viewModelScope.launch{
            subjectsDao.getAllSubjects().collect{
                    subj -> _allSubjects.value = subj
            }
        }
    }

    fun insertSubject(title: String){
        val subjectToInsert = SubjectEntity(title = title)
        viewModelScope.launch{
            subjectsDao.upsertSubject(subjectToInsert)
        }
    }

    fun deleteSubject(subjectEntity: SubjectEntity){
        viewModelScope.launch{
            subjectsDao.deleteSubject(subjectEntity)
        }
    }

    fun updateSubject(subjectEntity: SubjectEntity, newTitle: String){
        viewModelScope.launch{
            subjectsDao.upsertSubject(subjectEntity.copy(title = newTitle))
        }
    }

}