package com.papasmrufie.courseproject.presentation.ui


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.papasmrufie.courseproject.R
import com.papasmrufie.courseproject.data.database.AppDatabaseProvider
import com.papasmrufie.courseproject.data.entity.TestEntity
import com.papasmrufie.courseproject.presentation.slidingmenu.HamburgerMenu
import com.papasmrufie.courseproject.presentation.theme.CourseProjectTheme
import com.papasmrufie.courseproject.presentation.viewmodels.SubjectsViewModel
import com.papasmrufie.courseproject.presentation.viewmodels.TestsViewModel
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.day.DefaultDay
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue
import com.papasmrufie.courseproject.util.NofificationPermissionUtil


class MainActivity : ComponentActivity() {




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
    // String resources cant be used here, thats why it is hardcoded
    var defaultNotSelectedTest = TestEntity(
        testId = 0,
        testTitle = "Select a test day",
        testDescription = "Select a test day",
        subjectId = 0,
        dateTakePlace = 0
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val notificationPermissionUtil = NofificationPermissionUtil()
            notificationPermissionUtil.SetNotificationPermissions(this)

            CourseProjectTheme {
                HamburgerMenu {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomCalendarWithClick()


                    }


                }
            }

        }
    }


    //

    @Composable
    fun CustomCalendarWithClick(
        modifier: Modifier = Modifier,
    ) {

        val subjects by subjectsViewModel.allSubjects.collectAsState()
        val tests by testsViewModel.allTests.collectAsState()
        var dates = ArrayList<String>()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var currentlySelectedTest by remember { mutableStateOf(defaultNotSelectedTest) }

        for(i in tests){
            val date = i.dateTakePlace
            val formatted =  formatter.format(date)
            dates.add(formatted)
        }

        val dayContent: @Composable BoxScope.(DayState<DynamicSelectionState>) -> Unit = { dayState ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if(dayState.date.toString() in dates){
                    Box(
                        modifier = Modifier.border(2.dp, Color.Red)
                    ) {
                        DefaultDay(state = dayState, onClick = {
                            var testToShow = tests.filter { t -> formatter.format(t.dateTakePlace) == dayState.date.toString() }
                            currentlySelectedTest = testToShow[0]
                           // println("TEST !")
                        })
                    }
                }else{
                    DefaultDay(
                        state = dayState,
                        onClick = {
                            currentlySelectedTest = defaultNotSelectedTest
                            //println("${dayState.date}, ${dates[0]}, ${dayState.date.toString() == dates[0]}")
                        }
                    )
                }



            }
        }

        SelectableCalendar(
            modifier = modifier,
            dayContent = dayContent
        )

        Spacer(modifier = Modifier.height(5.dp))

        var titleofS = if(currentlySelectedTest != defaultNotSelectedTest){
            var subjToShow = subjects.filter { s -> s.id == currentlySelectedTest.subjectId }
            subjToShow.firstOrNull()?.title ?: stringResource(R.string.smiley)

        }else{
            stringResource(R.string.smiley)
        }


        if(currentlySelectedTest != defaultNotSelectedTest){
            DayInfoCard(t = currentlySelectedTest, subjTitle = titleofS)
        }else{
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .border(width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ){
                Text(stringResource(R.string.no_selected_test_day))
            }
        }

    }



    @Composable
    fun DayInfoCard(modifier: Modifier = Modifier, t: TestEntity, subjTitle: String) {
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .border(width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
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
                        color = Color.Black,//(194, 127, 2),
                        maxLines = 2,
                        minLines = 2
                    )

                    Text(
                        text = subjTitle,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        color = Color(150, 0, 19),
                        maxLines = 2,
                        minLines = 2
                    )
                    val formatter = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault())
                    val testDate = formatter.format(t.dateTakePlace).toString()
                    Text(
                        text = testDate,
                        fontWeight = FontWeight.Bold,
                        color = Color(5, 83, 161),
                        maxLines = 2,
                        minLines = 2
                    )
                }
                Row(
                    modifier = Modifier.widthIn(280.dp, 500.dp).heightIn(300.dp, 400.dp)

                ) {
                    Text(
                        text = t.testDescription,
                    )
                }
                Row(){
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(96, 123, 219))
                            .clickable(onClick = {

                                val intent = Intent(context, TestsActivity::class.java)
                                context.startActivity(intent)
                            })
                        ,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.detailed_test_information), color = Color.White, fontWeight = FontWeight.Bold)

                    }
                }


            }


        }
    }




}















