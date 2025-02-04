package com.mobitechs.notesapp.ui_layer.expense

// Core Composables

// Navigation

// Java Utilities


//import androidx.compose.material3.Chip
//import androidx.compose.material3.ChipDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobitechs.notesapp.Screen
import com.mobitechs.notesapp.data_layer.ExpenseDetails
import com.mobitechs.notesapp.util.ConfirmationDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenExpenseList(
    navController: NavController,
    stateExpense: StateExpense,
    onEventExpense: (EventExpense) -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var expenseToDelete by remember { mutableStateOf<ExpenseDetails?>(null) }

    val filteredExpenses = remember(searchQuery, selectedCategory, stateExpense.expenseList) {
        stateExpense.expenseList.filter {
            (searchQuery.isEmpty() || it.reason.contains(
                searchQuery,
                ignoreCase = true
            ) || it.categoryName!!.contains(
                searchQuery,
                ignoreCase = true
            )) &&
                    (selectedCategory == null || it.categoryName == selectedCategory)
        }
    }

    // Calculate category totals
    val categoryTotals = stateExpense.expenseList
        .groupBy { it.categoryName }
        .mapValues { entry ->
            entry.value.sumOf {
                it.amount.toIntOrNull() ?: 0 // Convert to Int, default to 0 if parsing fails
            }
        }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Expense Details",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            onEventExpense(EventExpense.SortListExpense)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Sort,
                                contentDescription = "Sort"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Search by reason or category") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.ScreenExpenseAdd.createRoute())
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category Filter Bubbles
            LazyRow(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categoryTotals.keys.toList()) { category ->
//                    Chip(
//                        onClick = {
//                            selectedCategory = if (selectedCategory == category) null else category
//                        },
//                        colors = ChipDefaults.chipColors(
//                            containerColor = if (selectedCategory == category) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
//                            contentColor = if (selectedCategory == category) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
//                        )
//                    ) {
//                        Text(
//                            text = "$category (${categoryTotals[category]})",
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                    }

                    Button(
                        onClick = {
                            selectedCategory = if (selectedCategory == category) null else category
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategory == category) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            contentColor = if (selectedCategory == category) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = MaterialTheme.shapes.small,
                        elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
                    ) {
                        Text(
                            text = "$category (${categoryTotals[category]})",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Filtered Expense List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredExpenses) { expense ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.elevatedCardElevation(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "${expense.date} ${expense.month} ${expense.year}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Category: ${expense.categoryName}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Rs.${expense.amount}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Reason: ${expense.reason}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = {
                                    navController.navigate(
                                        Screen.ScreenExpenseAdd.createRoute(
                                            expense
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
//                                    onEventExpense(EventExpense.DeleteExpense(expense))
                                    expenseToDelete = expense
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
            }
            if (showDeleteDialog) {
                ConfirmationDialog(
                    title = "Delete Expense",
                    message = "Are you sure you want to delete this expense?",
                    confirmButtonText = "Delete",
                    cancelButtonText = "Cancel",
                    onConfirm = {
                        expenseToDelete?.let {
                            onEventExpense(EventExpense.DeleteExpense(expenseToDelete!!))
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
}