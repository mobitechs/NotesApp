package com.mobitechs.notesapp.ui_layer.category

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobitechs.notesapp.data_layer.Category
import com.mobitechs.notesapp.ui_layer.expense.EventExpense
import com.mobitechs.notesapp.util.ShowToast

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenCategoryAdd(
    navController: NavController,
    stateCategory: StateCategory,
    onEvent: (EventCategory) -> Unit,
    categoryDetails: Category? // Nullable for editing or adding
) {
    val context = LocalContext.current

    var selectedCategoryName by remember { mutableStateOf(categoryDetails?.categoryName ?: "") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (selectedCategoryName.isEmpty()) {
                    ShowToast(context, "Please Enter Category")
                } else {
                    val event = if (categoryDetails != null) {
                        EventCategory.EditCategory(
                            categoryDetails.copy(
                                categoryName = selectedCategoryName,
                                categoryColorR = "0", // Replace with actual color values if needed
                                categoryColorG = "0",
                                categoryColorB = "0"
                            )
                        )
                    } else {
                        EventCategory.AddCategory(
                            categoryName = selectedCategoryName,
                            categoryColorR = "0", // Replace with actual color values if needed
                            categoryColorG = "0",
                            categoryColorB = "0"
                        )
                    }
                    onEvent(event)
                    navController.popBackStack() // Navigate back after saving
                }
            }) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                label = { Text("Category Name") },
                value = selectedCategoryName,
                onValueChange = { selectedCategoryName = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}