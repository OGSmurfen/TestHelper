package com.papasmrufie.courseproject.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.papasmrufie.courseproject.R
import com.papasmrufie.courseproject.data.database.AppDatabaseProvider
import com.papasmrufie.courseproject.data.entity.SubjectEntity
import com.papasmrufie.courseproject.data.entity.TestEntity
import com.papasmrufie.courseproject.presentation.slidingmenu.HamburgerMenu
import com.papasmrufie.courseproject.presentation.theme.CourseProjectTheme
import com.papasmrufie.courseproject.presentation.viewmodels.SubjectsViewModel
import com.papasmrufie.courseproject.presentation.viewmodels.TestsViewModel
import com.papasmrufie.courseproject.util.NotificationWorkerHandler
import java.text.SimpleDateFormat
import java.util.Locale

class TestsActivity : ComponentActivity() {
    private val db by lazy { AppDatabaseProvider.getDatabase(applicationContext) }
    private val subjectsViewModel by viewModels<SubjectsViewModel>(
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
    private val testsViewModel by viewModels<TestsViewModel>(
        factoryProducer = {
            object: ViewModelProvider.Factory{
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                ): T {
                    return TestsViewModel(db.testsDao) as T
                }
            }
        }
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{

            var showDialog by remember { mutableStateOf(false) }

            val subjects by subjectsViewModel.allSubjects.collectAsState()
            val tests by testsViewModel.allTests.collectAsState()


            CourseProjectTheme {
                HamburgerMenu {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(6f)
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,

                            ) {
                                items(tests) {
                                    t ->
                                    TestListItem(t,
                                        subjects.find { it.id == t.subjectId }?.title ?: t.subjectId.toString(),
                                        onDelete = {testsViewModel.deleteTest(t)},
                                        subjects = subjects
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                }
                            }
                        }
                        Row (
                            modifier = Modifier
                                .weight(1f)
                        ){ Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color(96, 123, 219))
                                .clickable(onClick = {

                                    showDialog = true
                                }),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_test_c_d),
                                tint = Color.White
                            )
                        } }
                    }

                    if (showDialog) {
                        AddTestDialog(
                            showDialogChange = {newVal -> showDialog = newVal},
                            subjects = subjects
                        )
                    }





                }

            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddTestDialog(
        testToModify: TestEntity? = null,
        subjects: List<SubjectEntity>,
        showDialogChange: (Boolean) -> Unit
    ) {
        val cantSetupNotificationText = stringResource(R.string.cant_setup_notification)
        val successfulNotificationSetupText = stringResource(R.string.successful_notification_setup)
        var testTitleUserInput by remember { mutableStateOf(testToModify?.testTitle ?: "") }
        var testDescriptionUserInput by remember { mutableStateOf(testToModify?.testDescription ?: "") }
        var selectedSubject by remember { mutableStateOf("") }
        var selectedSubjectId by remember { mutableIntStateOf(testToModify?.subjectId ?: 0) }
        var selectedDate = if(testToModify != null){
            remember {mutableStateOf(testToModify.dateTakePlace.toString())}
        }else{
            remember {mutableStateOf("")}
        }
        //var selectedDateMillis by remember { mutableStateOf(0) }
        var datePickerVisible by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showDialogChange(false)},
            confirmButton = {
                Button(onClick = {
                    if(testTitleUserInput.isNotBlank() &&
                        testDescriptionUserInput.isNotBlank()
                        && selectedSubject.isNotBlank())
                    {
                            if(testToModify != null){
                                testsViewModel.updateTest(
                                    testToModify,
                                    testTitleUserInput,
                                    testDescriptionUserInput,
                                    selectedSubjectId,
                                    selectedDate.value.toLong()
                                )
                                showDialogChange(false)
                            }else{
                                testsViewModel.insertTest(
                                    testTitleUserInput,
                                    testDescriptionUserInput,
                                    selectedSubjectId,
                                    selectedDate.value.toLong()
                                )
                                val oneDayInMillis = 86400000
                                var notificationDelay = selectedDate.value.toLong() - System.currentTimeMillis() - oneDayInMillis


                                if(notificationDelay > 0){
                                    val notificationWorkerHandler = NotificationWorkerHandler(this)
                                    notificationWorkerHandler.setupWorkDelayed(notificationDelay)
                                    Toast.makeText(this, successfulNotificationSetupText, Toast.LENGTH_LONG).show()
                                }else{
                                    Toast.makeText(this, cantSetupNotificationText, Toast.LENGTH_LONG).show()
                                }

                                showDialogChange(false)
                            }

                    }

                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                Button(onClick = { showDialogChange(false) }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = { Text(stringResource(R.string.add_new_test)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = testTitleUserInput,
                        onValueChange = { testTitleUserInput = it },
                        label = { Text(stringResource(R.string.test_title)) },
                        maxLines = 1,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = testDescriptionUserInput,
                        onValueChange = { testDescriptionUserInput = it },
                        label = { Text(stringResource(R.string.test_description)) },
                        minLines = 5,
                        maxLines = 10
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    //Date picker:
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            //.clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(5.dp)),
                        onClick = {datePickerVisible = true},
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Row {
                            Text(text =
                            if(selectedDate.value.isEmpty()) {
                                stringResource(R.string.no_date_selected)
                            } else {
                                val formatter = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault())
                                val date = selectedDate.value.toLong()
                                formatter.format(date)
                                //Date(selectedDate.toLong()).toString()
                            },
                                color = Color.DarkGray,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = stringResource(R.string.pick_date_c_d),
                                tint = Color.DarkGray
                            )
                        }


                    }
                    if (datePickerVisible) {

                        DatePickerModal(
                            onDateSelected = { d -> selectedDate.value = d.toString()
                                             datePickerVisible = false
                                //println(Date(selectedDate.toLong()))
                                             },
                            onDismiss = { datePickerVisible = false }
                        )
                       
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dropdown for category selection:
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedSubject,
                            onValueChange = {},
                            label = { Text(text = stringResource(R.string.select_subject)) },
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                            },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            scrollState = rememberScrollState()
                        ) {
                            subjects.forEach { s ->
                                DropdownMenuItem(
                                    text = { Text(s.title) },
                                    onClick = {
                                        selectedSubject = s.title.toString()
                                        selectedSubjectId = s.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }





                }
            }
        )
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DatePickerModal(
        onDateSelected: (Long?) -> Unit,
        onDismiss: () -> Unit
    ) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                // Disable confirmation if no date is selected
                if (datePickerState.selectedDateMillis == null) {
                    TextButton(onClick = { /* Do nothing */ }) {
                        Text("OK", color = Color.Gray) // Optional: Style to indicate it's disabled
                    }
                } else {
                    TextButton(onClick = {
                        onDateSelected(datePickerState.selectedDateMillis) // Pass the selected date
                        onDismiss() // Dismiss the dialog
                    }) {
                        Text("OK")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

@Composable
fun TestListItem(t: TestEntity,
                 subjTitle: String,
                 onDelete: () -> Unit,
                 subjects: List<SubjectEntity>
) {
    var showModificationDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable(
                onClick = {
                        showModificationDialog = true
                }
            )
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            ) {
                //Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = t.testTitle,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = Color.Black //(194, 127, 2)

                )

                Text(
                    text = subjTitle,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    color = Color(150, 0, 19)
                )
                val formatter = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault())
                val testDate = formatter.format(t.dateTakePlace).toString()
                Text(
                    text = testDate,
                    fontWeight = FontWeight.Bold,
                    color = Color(5, 83, 161)
                )
                IconButton(
                    onClick = { onDelete() },
                    //modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_test_c_d),
                        tint = Color.Red
                    )
                }
            }
            Row(
                modifier = Modifier.widthIn(280.dp, 500.dp)
            ) {
                Text(
                    text = t.testDescription,
                )
            }

        }


    }


    if(showModificationDialog){
        AddTestDialog(
            testToModify = t,
            showDialogChange = {newVal ->
                showModificationDialog = newVal},
            subjects = subjects
        )
    }
}



}
