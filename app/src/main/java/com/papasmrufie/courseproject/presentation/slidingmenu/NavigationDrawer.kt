package com.papasmrufie.courseproject.presentation.slidingmenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.papasmrufie.courseproject.R

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = stringResource(R.string.menu), fontSize = 60.sp, color = Color.White)
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(
        modifier
    ) {
        items(items){
            i ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        onItemClick(i)
                    }
                    .padding(16.dp)
            ){
                Icon(
                    imageVector = i.icon,
                    contentDescription = i.contentDescription
                )
                Spacer(
                    modifier = Modifier.width(16.dp)
                )
                Text(
                    text = i.title,
                    style = itemTextStyle,
                    modifier = Modifier
                        .weight(1f),
                    color = Color.White
                )

            }

        }
    }
}