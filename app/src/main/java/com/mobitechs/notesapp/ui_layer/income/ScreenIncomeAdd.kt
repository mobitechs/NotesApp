package com.mobitechs.notesapp.ui_layer.income

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobitechs.notesapp.data_layer.ExpenseDetails
import com.mobitechs.notesapp.data_layer.Income
import com.mobitechs.notesapp.ui_layer.expense.EventExpense
import com.mobitechs.notesapp.util.DropdownField
import com.mobitechs.notesapp.util.ShowToast

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenIncomeAdd(
    navController: NavController,
    stateIncome: StateIncome,
    onEventIncome: (EventIncome) -> Unit,
    incomeDetails: Income?
) {
    val context = LocalContext.current
    val listOfMonth = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    //var selectedMonth by remember { mutableStateOf(listOfMonth[0]) }

    val listOfYear = listOf(
        "2018", "2019", "2020", "2021", "2022", "2023", "2024",
        "2025", "2026", "2027", "2028", "2029", "2030"
    )
   // var selectedYear by remember { mutableStateOf(listOfYear[0]) }

    var selectedIncome by remember { mutableStateOf(incomeDetails?.incomeAmt ?: "") }
    var selectedMonth by remember { mutableStateOf(incomeDetails?.incomeMonth ?: "") }
    var selectedYear by remember { mutableStateOf(incomeDetails?.incomeYear ?: "") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {

                if (selectedIncome.isEmpty()) {
                    ShowToast(context, "Please Enter Income Amount")
                } else {
                    val event = if (incomeDetails != null) {
                        // Create a new updated object for editing
                        EventIncome.EditIncome(
                            incomeDetails.copy(
                                incomeAmt = selectedIncome,
                                incomeYear = selectedYear,
                                incomeMonth = selectedMonth
                            )
                        )
                    } else {
                        // Create a new expense for adding
                        EventIncome.AddIncome(
                            incomeAmt = selectedIncome,
                            incomeYear = selectedYear,
                            incomeMonth = selectedMonth
                        )

                    }
                    onEventIncome(event)
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

            DropdownField(
                label = "Year",
                options = listOfYear,
                selectedOption = selectedYear,
                onOptionSelected = { selectedYear = it },
                context = context
            )

            Spacer(modifier = Modifier.height(20.dp))

            DropdownField(
                label = "Month",
                options = listOfMonth,
                selectedOption = selectedMonth,
                onOptionSelected = { selectedMonth = it },
                context = context
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                label = { Text(text = "Income Amount") },
                value = selectedIncome,
                onValueChange = { selectedIncome = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}