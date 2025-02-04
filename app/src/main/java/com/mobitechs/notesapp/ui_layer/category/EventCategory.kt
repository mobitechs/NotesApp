package com.mobitechs.notesapp.ui_layer.category

import com.mobitechs.notesapp.data_layer.Category
import com.mobitechs.notesapp.data_layer.ExpenseDetails
import com.mobitechs.notesapp.ui_layer.expense.EventExpense

sealed interface EventCategory {

    object SortCategory : EventCategory

    data class DeleteCategory(val category: Category) : EventCategory

    data class AddCategory(
        val categoryName: String,
        val categoryColorR: String,
        val categoryColorG: String,
        val categoryColorB: String

    ) : EventCategory

    data class EditCategory(val categoryDetails: Category) : EventCategory
}



