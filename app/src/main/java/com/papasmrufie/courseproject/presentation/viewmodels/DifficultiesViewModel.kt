package com.papasmrufie.courseproject.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papasmrufie.courseproject.data.dao.DifficultiesDao
import com.papasmrufie.courseproject.data.entity.DifficultyEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DifficultiesViewModel(private val difficultiesDao: DifficultiesDao) : ViewModel() {
    private val _allDifficulties = MutableStateFlow<List<DifficultyEntity>>(emptyList())
    val allDifficulties: StateFlow<List<DifficultyEntity>> = _allDifficulties

    init {
        viewModelScope.launch{
            difficultiesDao.getAllDifficulties().collect{
                d -> _allDifficulties.value = d
            }
        }
    }


}