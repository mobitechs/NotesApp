package com.mobitechs.notesapp.ui_layer.income

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobitechs.notesapp.data_layer.Income
import com.mobitechs.notesapp.data_layer.NoteDatabase
import com.mobitechs.notesapp.ui_layer.category.EventCategory
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
class ViewModelIncome @Inject constructor(noteDatabase: NoteDatabase) : ViewModel() {

    val dao = noteDatabase.incomeDao
    val isSortedByDateAdded = MutableStateFlow(true)
    val income = isSortedByDateAdded.flatMapLatest {
        if (it) {
            dao.getAllIncome()
        } else {
            dao.getAllIncomeByIncomeAmt()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _StateIncome = MutableStateFlow(StateIncome())
    val stateIncome = combine(
        _StateIncome,
        isSortedByDateAdded,
        income
    ) { incomeState, isSortedByDateAdded, incomes ->
        incomeState.copy(incomeList = incomes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StateIncome())


    fun onEvent(event: EventIncome) {
        when (event) {
            is EventIncome.DeleteIncome -> {
                viewModelScope.launch {
                    dao.delete(event.income)
                }
            }

            is EventIncome.AddIncome -> {
                val income = Income(
                    incomeMonth = event.incomeMonth,
                    incomeYear = event.incomeYear,
                    incomeAmt = event.incomeAmt,
                    dateDated = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    dao.upsert(income)
                    _StateIncome.update { currentState ->
                        currentState.copy(
                            incomeList = currentState.incomeList + income // Append the new expense to the list
                        )
                    }
                }
            }

            is EventIncome.EditIncome -> {
                viewModelScope.launch {
                    dao.upsert(event.incomeDetails)
                }
            }

            EventIncome.SortListIncome -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
        }
    }


}