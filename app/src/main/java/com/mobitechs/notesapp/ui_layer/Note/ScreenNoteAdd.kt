package com.mobitechs.notesapp.ui_layer.Note

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobitechs.notesapp.util.ShowDatePicker
import com.mobitechs.notesapp.util.ShowToast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenNoteAdd(
    navController: NavController,
    state: StateNote,
    onEvent: (EventNote) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayDate = dateFormat.format(calendar.time)
    var selectedDate by remember { mutableStateOf(todayDate) }

    state.addedDate.value = selectedDate

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {

                ShowToast(context, "Saved")
                onEvent(
                    EventNote.SaveNote(
                        title = state.title.value,
                        dips = state.disp.value
                    )
                )
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(Color.Red)
        ) {
            // Amount TextField
            TextField(
                label = { Text(text = "Amount") },
                value = state.title.value,
                onValueChange = { state.title.value = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Expense Reason TextField
            TextField(
                label = { Text(text = "Expense Reason") },
                value = state.disp.value,
                onValueChange = { state.disp.value = it },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Select Date TextField
            TextField(
                label = { Text(text = "Select Date") },
                value = state.addedDate.value,
                onValueChange = { state.addedDate.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        ShowDatePicker(
                            context = context,
                            onDateSelected = { year, month, dayOfMonth ->
                                val newCalendar = Calendar.getInstance()
                                newCalendar.set(year, month, dayOfMonth)
                                selectedDate = dateFormat.format(newCalendar.time)
                                Log.d("DatePicker", "Updated date: $selectedDate") // Debug log
                            }
                        )
                    },
                //  readOnly = true
            )
        }
    }
}