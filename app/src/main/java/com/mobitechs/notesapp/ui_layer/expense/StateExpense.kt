package com.mobitechs.notesapp.ui_layer.expense

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mobitechs.notesapp.data_layer.Category
import com.mobitechs.notesapp.data_layer.ExpenseDetails

data class StateExpense (
    val categoryList:List<Category> = emptyList(),
    val expenseList:List<ExpenseDetails> = emptyList(),
    val categoryId:MutableState<String> = mutableStateOf(""),
    val categoryName:MutableState<String> = mutableStateOf(""),
    val amount:MutableState<String> = mutableStateOf(""),
    val reason:MutableState<String> = mutableStateOf(""),
    val date:MutableState<String> = mutableStateOf(""),
    val month:MutableState<String> = mutableStateOf(""),
    val year:MutableState<Int> = mutableStateOf(0),
    val dateDated: MutableState<Long> = mutableStateOf(0L)

)
