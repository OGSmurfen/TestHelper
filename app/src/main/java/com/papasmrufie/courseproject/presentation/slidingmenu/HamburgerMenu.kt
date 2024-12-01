package com.papasmrufie.courseproject.presentation.slidingmenu

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.papasmrufie.courseproject.R
import com.papasmrufie.courseproject.presentation.ui.AboutActivity
import com.papasmrufie.courseproject.presentation.ui.MainActivity
import com.papasmrufie.courseproject.presentation.ui.SubjectsActivity
import com.papasmrufie.courseproject.presentation.ui.TestsActivity
import kotlinx.coroutines.launch


@Composable
fun HamburgerMenu(
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if(drawerState.isOpen){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Blue.copy(alpha = 0.7f),
                                    Color.Cyan.copy(alpha = 0.7f)
                                )
                            )
                        )
                ) {

                    Column {
                        DrawerHeader()
                        DrawerBody(
                            items = listOf(
                                // I have consciously not put the 'id' strings to strings.xml
                                // because they are responsible for the logic of the app and should not be translated nor exposed
                                MenuItem(
                                    id = "home",
                                    title = stringResource(id = R.string.home),
                                    contentDescription = stringResource(id = R.string.home_content_description),
                                    icon = Icons.Default.Home
                                ),
                                MenuItem(
                                    id = "subjects",
                                    title = stringResource(id = R.string.subjects),
                                    contentDescription = stringResource(id = R.string.subjects_c_d),
                                    icon = Icons.Default.Create
                                ),
                                MenuItem(
                                    id = "tests",
                                    title = stringResource(id = R.string.tests),
                                    contentDescription = stringResource(id = R.string.tests_c_d),
                                    icon = Icons.Default.CheckCircle
                                ),
                                MenuItem(
                                    id = "about",
                                    title = stringResource(id = R.string.about),
                                    contentDescription = stringResource(id = R.string.about_c_d),
                                    icon = Icons.Default.Info
                                )
                            ),
                            onItemClick = {
                                //println("Clicked on ${it.title}")

                                // Check for these IDs is not included in strings.xml consciously. Stated above
                                when (it.id) {
                                    "home" -> {
                                        val intent = Intent(context, MainActivity::class.java)
                                        context.startActivity(intent)
                                    }
                                    "subjects" -> {
                                        val intent = Intent(context, SubjectsActivity::class.java)
                                        context.startActivity(intent)
                                    }
                                    "tests" -> {
                                        val intent = Intent(context, TestsActivity::class.java)
                                        context.startActivity(intent)
                                    }
                                    "about" -> {
                                        val intent = Intent(context, AboutActivity::class.java)
                                        context.startActivity(intent)
                                    }
                                }

                            }
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                AppBar(
                    onNavigationIconClick = {
                        //println("CLICKED HAMBURGER")
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            content = {
                    padding ->
                Column(
                    modifier = Modifier.padding(padding)
                ) {

                    content()
                }

            }
        )
    }
}