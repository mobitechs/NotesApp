package com.mobitechs.notesapp.ui_layer.income

import com.mobitechs.notesapp.data_layer.Category
import com.mobitechs.notesapp.data_layer.Income
import com.mobitechs.notesapp.ui_layer.category.EventCategory

sealed interface EventIncome {

    object SortListIncome : EventIncome

    data class DeleteIncome(val income: Income) : EventIncome

    data class AddIncome(
        val incomeMonth: String,
        val incomeYear: String,
        val incomeAmt: String
    ) : EventIncome

    data class EditIncome(val incomeDetails: Income) : EventIncome
}