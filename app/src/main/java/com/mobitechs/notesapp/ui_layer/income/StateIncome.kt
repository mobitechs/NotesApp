package com.mobitechs.notesapp.ui_layer.income

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mobitechs.notesapp.data_layer.Income

data class StateIncome(
    val incomeList: List<Income> = emptyList(),
    val incomeMonth: MutableState<String> = mutableStateOf(""),
    val incomeYear: MutableState<String> = mutableStateOf(""),
    val incomeAmt: MutableState<String> = mutableStateOf(""),
    val dateDated: MutableState<Long> = mutableStateOf(0L)
)