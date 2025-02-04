package com.mobitechs.notesapp.ui_layer.expense

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobitechs.notesapp.data_layer.ExpenseDetails
import com.mobitechs.notesapp.util.DropdownField
import com.mobitechs.notesapp.util.ShowDatePicker
import com.mobitechs.notesapp.util.ShowToast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenExpenseAdd(
    navController: NavController,
    stateExpense: StateExpense,
    onEventExpense: (EventExpense) -> Unit,
    expenseDetails: ExpenseDetails?

) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

    var categoryId by remember { mutableStateOf(expenseDetails?.categoryId ?: "") }
    var selectedCategoryName by remember { mutableStateOf(expenseDetails?.categoryName ?: "") }
    var amount by remember { mutableStateOf(expenseDetails?.amount ?: "") }
    var reason by remember { mutableStateOf(expenseDetails?.reason ?: "") }
    var date by remember { mutableStateOf(expenseDetails?.date ?: "") }
    var month by remember { mutableStateOf(expenseDetails?.month ?: "") }
    var year by remember { mutableStateOf(expenseDetails?.year?.toString() ?: "") }


    // Extract category names from the category list
    val categoryList = stateExpense.categoryList
    val categoryNames = categoryList.map { it.categoryName }

    // State variables to track selected category and date
    var selectedCategoryId by remember { mutableStateOf("") }


    var selectedFullDate by remember {
        mutableStateOf(
            if (expenseDetails != null) {
                "${expenseDetails.date}-${expenseDetails.month}-${expenseDetails.year}"
            } else {
                dateFormat.format(Calendar.getInstance().time)
            }
        )
    }

    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH).toString()) }
    var selectedMon by remember {
        mutableStateOf(
            SimpleDateFormat(
                "MMM",
                Locale.getDefault()
            ).format(calendar.time)
        )
    }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR).toString()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (amount.isEmpty()) {
                    ShowToast(context, "Please Enter Amount")
                } else {
                    // Update date, month, year variables
                    date = selectedDay
                    month = selectedMon
                    year = selectedYear

                    val event = if (expenseDetails != null) {
                        // Create a new updated object for editing
                        EventExpense.EditExpense(
                            expenseDetails.copy(
                                categoryId = selectedCategoryId,
                                categoryName = selectedCategoryName,
                                amount = amount,
                                reason = reason,
                                date = date,
                                month = month,
                                year = year.toInt()
                            )
                        )
                    } else {
                        // Create a new expense for adding
                        EventExpense.AddExpense(
                            categoryId = selectedCategoryId,
                            categoryName = selectedCategoryName,
                            amount = amount,
                            reason = reason,
                            date = date,
                            month = month,
                            year = year.toInt()
                        )
                    }
                    onEventExpense(event)
                    navController.popBackStack()
                }
            }) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Dropdown for category selection
            DropdownField(
                label = "Category",
                options = categoryNames,
                selectedOption = selectedCategoryName,
                onOptionSelected = { selectedName ->
                    selectedCategoryName = selectedName
                    selectedCategoryId =
                        (categoryList.firstOrNull { it.categoryName == selectedName }?.categoryId
                            ?: "").toString()
                },
                context = context
            )

            Spacer(modifier = Modifier.height(20.dp))

            // TextField for expense amount
            TextField(
                label = { Text(text = "Expense Amount") },
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // TextField for expense reason
            TextField(
                label = { Text(text = "Expense Reason") },
                value = reason,
                onValueChange = { reason = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Button for date picker
            Button(
                onClick = {
                    ShowDatePicker(
                        context = context,
                        onDateSelected = { year, month, dayOfMonth ->
                            val newCalendar = Calendar.getInstance().apply {
                                set(year, month, dayOfMonth)
                            }
                            selectedFullDate = dateFormat.format(newCalendar.time)
                            selectedDay = dayOfMonth.toString()
                            selectedMon = SimpleDateFormat(
                                "MMM",
                                Locale.getDefault()
                            ).format(newCalendar.time)
                            selectedYear = year.toString()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp)
                    .background(Color(0xFFE2E1E8))
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, size.height - strokeWidth / 2),
                            end = Offset(size.width, size.height - strokeWidth / 2),
                            strokeWidth = strokeWidth
                        )
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
                shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
            ) {
                Text(
                    text = selectedFullDate,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}