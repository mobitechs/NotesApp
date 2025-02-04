package com.mobitechs.notesapp.ui_layer.income

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobitechs.notesapp.Screen
import com.mobitechs.notesapp.data_layer.Category
import com.mobitechs.notesapp.data_layer.Income
import com.mobitechs.notesapp.ui_layer.category.EventCategory
import com.mobitechs.notesapp.ui_layer.expense.EventExpense
import com.mobitechs.notesapp.util.ConfirmationDialog
import com.mobitechs.notesapp.util.ShowToast

@Composable
fun ScreenIncomeList(
//    modifier: Modifier,
    navController: NavController,
    stateIncome: StateIncome,
    onEventIncome: (EventIncome) -> Unit
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var incomeToDelete by remember { mutableStateOf<Income?>(null) }


    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Income Listing",
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    fontSize = 17.sp
                )
                IconButton(onClick = {
                    onEventIncome(EventIncome.SortListIncome)

                }) {
                    Icon(imageVector = Icons.AutoMirrored.Rounded.Sort, contentDescription = null)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
//                navController.navigate(Screen.ScreenIncomeAdd)
                navController.navigate(Screen.ScreenIncomeAdd.createRouteIncome())
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = it
        ) {
            items(stateIncome.incomeList) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Income of ${it.incomeMonth} ${it.incomeYear}")
                        Text(text = "Rs.${it.incomeAmt}", fontSize = 21.sp)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = {
                            navController.navigate(
                                Screen.ScreenIncomeAdd.createRouteIncome(
                                    it
                                )
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = {
                           // onEventIncome(EventIncome.DeleteIncome(it))
                            incomeToDelete = it
                            showDeleteDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                }
            }
        }
        if (showDeleteDialog) {
            ConfirmationDialog(
                title = "Delete Income",
                message = "Are you sure you want to delete this income details?",
                confirmButtonText = "Delete",
                cancelButtonText = "Cancel",
                onConfirm = {
                    onEventIncome?.let {
                        onEventIncome(EventIncome.DeleteIncome(incomeToDelete!!))
                    }
                    showDeleteDialog = false
                },
                onCancel = { showDeleteDialog = false },
                isDialogVisible = true,
                onDismiss = { showDeleteDialog = false }
            )
        }
    }

}