package com.papasmrufie.courseproject.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.papasmrufie.courseproject.data.database.AppDatabaseProvider
import com.papasmrufie.courseproject.data.entity.SubjectEntity
import com.papasmrufie.courseproject.presentation.slidingmenu.HamburgerMenu
import com.papasmrufie.courseproject.presentation.theme.CourseProjectTheme
import com.papasmrufie.courseproject.presentation.viewmodels.SubjectsViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.papasmrufie.courseproject.R
import androidx.compose.material3.OutlinedTextField

class SubjectsActivity : ComponentActivity() {

    private val db by lazy { AppDatabaseProvider.getDatabase(applicationContext) }

    private val viewModel by viewModels<SubjectsViewModel>(
        factoryProducer = {
            object: ViewModelProvider.Factory{
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                ): T {
                    return SubjectsViewModel(db.subjectsDao) as T
                }
            }
        }
    )


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState )
        enableEdgeToEdge()
        setContent{
            CourseProjectTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar_add(viewModel = viewModel)
                    },
                    content = { innerPadding ->
                        HamburgerMenu{

                            SubjectsScreen(
                                modifier = Modifier.padding(innerPadding),
                                viewModel = viewModel
                            )


                        }

                    }
                )

            }


        }
    }


    @Composable
    fun SubjectsScreen(
        modifier: Modifier,
        viewModel: SubjectsViewModel
    ) {
        val subjects by viewModel.allSubjects.collectAsState(initial = emptyList())

        SubjectsList(
            subjects = subjects,
            viewModel = viewModel,
            modifier = modifier
        )
    }

    @Composable
    fun SubjectsList(subjects: List<SubjectEntity>, viewModel: SubjectsViewModel, modifier: Modifier) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp)
        ){
            items(subjects){
                    subj ->
                SubjectsListItem(
                    subject = subj,
                    onDelete = {
                        viewModel.deleteSubject(subj)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }

    @Composable
    fun SubjectsListItem(
        subject: SubjectEntity,
        onDelete: () -> Unit,
        //onStatsChange !!!! implement
    ){
        val showModifySubjectDialog = remember { mutableStateOf(false) }
        var newSubjectTitle by remember { mutableStateOf(subject.title) }

        Column(
            modifier = Modifier.fillMaxSize()
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp)
                .clickable(onClick = {
                    showModifySubjectDialog.value = true

                })
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    text = subject.title,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { onDelete() },
                    //modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_subject_c_d),
                        tint = Color.Red
                    )
                }
            }

        }

        if (showModifySubjectDialog.value) {
            AlertDialog(
                onDismissRequest = { showModifySubjectDialog.value = false }, // Close the dialog on outside click
                title = { Text(stringResource(R.string.modfy_subject)) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newSubjectTitle,
                            onValueChange = { newSubjectTitle = it },
                            label = { Text(stringResource(R.string.change_subject_title)) },
                            maxLines = 1,
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if(newSubjectTitle.isNotBlank()){
                            viewModel.updateSubject(subject, newSubjectTitle)
                        }
                        showModifySubjectDialog.value = false
                    }) {
                        Text(stringResource(R.string.OK))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        newSubjectTitle = subject.title
                        showModifySubjectDialog.value = false
                    }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }






}
