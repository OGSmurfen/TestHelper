package com.papasmrufie.courseproject.presentation.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.papasmrufie.courseproject.R
import com.papasmrufie.courseproject.presentation.slidingmenu.HamburgerMenu
import com.papasmrufie.courseproject.presentation.theme.CourseProjectTheme
import com.papasmrufie.courseproject.util.NotificationWorkerHandler

class AboutActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            CourseProjectTheme {
                HamburgerMenu {
                    // Experiment with notifications:
//                    val notificationWorkerHandler = NotificationWorkerHandler(this)
//                    notificationWorkerHandler.setupWorkDelayed(10000)

                    val allImagesList = listOf(R.drawable.suika_image, R.drawable.slick_rick, R.drawable.flappy_tank)
                    val allUris = listOf(stringResource(R.string.papasmurfie_itch),
                        stringResource(R.string.google_play_slickrick), stringResource(R.string.flappytank_steam))

                    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                        item{
                            Text(
                                text = stringResource(R.string.about_information),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        val pairedList = allImagesList.zip(allUris)
                        items(pairedList){
                                (image, uri)->
                            ScrollableImage(imageResId = image, uri = uri.toString())
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                    }


                }
            }
        }
    }





    @Composable
    fun ScrollableImage(modifier: Modifier = Modifier, imageResId: Int, uri:String ) {
        val uriHandler = LocalUriHandler.current

        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(150.dp)
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray)
                .border(2.dp, Color.Black)
                .clickable(onClick = { uriHandler.openUri(uri) })
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = stringResource(R.string.click_to_play_more),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Spacer(Modifier.height(50.dp) )
            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                text = stringResource(R.string.click_to_play_game),
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(0f, 0f),
                        blurRadius = 5f
                    )
                )
            )
        }
    }


}