package com.mobitechs.notesapp.ui_layer.expense

import com.mobitechs.notesapp.data_layer.ExpenseDetails

sealed interface EventExpense {
    object SortListExpense : EventExpense
    data class DeleteExpense(val expenseDetails: ExpenseDetails) : EventExpense

    data class AddExpense(
        val categoryId: String,
        val categoryName: String,
        val amount: String,
        val reason: String,
        val date: String,
        val month: String,
        val year: Int
    ) : EventExpense

    data class EditExpense(val expenseDetails: ExpenseDetails) : EventExpense
}