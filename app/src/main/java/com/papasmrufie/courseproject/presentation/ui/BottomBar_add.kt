package com.papasmrufie.courseproject.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.papasmrufie.courseproject.R
import com.papasmrufie.courseproject.presentation.viewmodels.SubjectsViewModel

@Composable
fun BottomBar_add(viewModel: SubjectsViewModel) {
    BottomAppBar {
        var subjTitleInField by remember { mutableStateOf("") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            OutlinedTextField(
                value = subjTitleInField,
                onValueChange = {subjTitleInField = it},
                label = { Text(text = stringResource(R.string.subject_title)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                maxLines = 1
            )
            Button(
                onClick = {
                    if(subjTitleInField.isNotBlank()){
                        viewModel.insertSubject(subjTitleInField)
                        subjTitleInField = ""
                    }
                },
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(text = stringResource(R.string.add))
            }
        }


    }
}