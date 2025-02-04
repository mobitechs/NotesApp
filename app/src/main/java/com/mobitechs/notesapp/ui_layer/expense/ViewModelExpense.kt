package com.mobitechs.notesapp.ui_layer.expense

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobitechs.notesapp.data_layer.ExpenseDetails
import com.mobitechs.notesapp.data_layer.NoteDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelExpense @Inject constructor(noteDatabase: NoteDatabase) : ViewModel() {

    val dao = noteDatabase.expenseDetailsDao

    val categoryList = dao.getCategoryList().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isSortByDateAdded = MutableStateFlow(false)
    val expenseDetails = isSortByDateAdded.flatMapLatest {
        if (it) {
            dao.getAllExpenseDetails()
        } else {
            dao.getAllExpenseAmountWise()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _StateExpenseDetails = MutableStateFlow(StateExpense())
    val stateExpenseDetails = combine(
        _StateExpenseDetails, isSortByDateAdded, expenseDetails,categoryList
    ) { expenseDetailsState, isSortByDateAdded, expenseDetails, categoryList ->

        expenseDetailsState.copy(expenseList = expenseDetails, categoryList = categoryList)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StateExpense())

    fun onEvent(event: EventExpense) {
        when (event) {
            is EventExpense.DeleteExpense -> {
                viewModelScope.launch {
                    dao.delete(event.expenseDetails)
                }
            }

            is EventExpense.AddExpense -> {
                val expense = ExpenseDetails(
                    categoryId = event.categoryId,
                    categoryName = event.categoryName,
                    amount = event.amount,
                    reason = event.reason,
                    date = event.date,
                    month = event.month,
                    year = event.year,
                    dateDated = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    dao.upsert(expense) // Insert or update in the database
                    _StateExpenseDetails.update { currentState ->
                        currentState.copy(
                            expenseList = currentState.expenseList + expense // Append the new expense to the list
                        )
                    }
                }
            }


            is EventExpense.EditExpense -> {
                viewModelScope.launch {
                    dao.upsert(event.expenseDetails)
                }
            }

            EventExpense.SortListExpense -> {
                isSortByDateAdded.value = !isSortByDateAdded.value
            }


        }
    }

}