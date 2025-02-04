package com.mobitechs.notesapp.ui_layer.category

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mobitechs.notesapp.data_layer.Category

data class StateCategory(
    val categoryList: List<Category> = emptyList(),
    val categoryName: MutableState<String> = mutableStateOf(""),
    val categoryColorR: MutableState<String> = mutableStateOf(""),
    val categoryColorG: MutableState<String> = mutableStateOf(""),
    val categoryColorB: MutableState<String> = mutableStateOf(""),
    val dateDated: MutableState<String> = mutableStateOf("")
)
