package com.mobitechs.notesapp.ui_layer.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobitechs.notesapp.data_layer.Category
import com.mobitechs.notesapp.data_layer.ExpenseDetails
import com.mobitechs.notesapp.data_layer.NoteDatabase
import com.mobitechs.notesapp.ui_layer.expense.EventExpense
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
class ViewModelCategory @Inject constructor(noteDatabase: NoteDatabase) : ViewModel() {

    val dao = noteDatabase.categoryDao
    val isSortedByDateAdded = MutableStateFlow(true)
    var category = isSortedByDateAdded.flatMapLatest {
        if (it) {
            dao.getAllNoteOrderedDateAdded()
        } else {
            dao.getAllNoteOrderedTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    val _StateCategory = MutableStateFlow(StateCategory())
    val stateCategory = combine(
        _StateCategory,
        isSortedByDateAdded,
        category
    ) { categoryState, isSortedByDateAdded, categories ->
        categoryState.copy(categoryList = categories)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StateCategory())


    fun onEvent(event: EventCategory) {
        when (event) {
            is EventCategory.DeleteCategory -> {
                viewModelScope.launch {
                    dao.delete(event.category)
                }
            }

            is EventCategory.AddCategory -> {
                val category = Category(
                    categoryName = event.categoryName,
                    categoryColorR = event.categoryColorR,
                    categoryColorG = event.categoryColorG,
                    categoryColorB = event.categoryColorB,
                    dateDated = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    dao.upsert(category)
                    _StateCategory.update { currentState ->
                        currentState.copy(
                            categoryList = currentState.categoryList + category // Append the new expense to the list
                        )
                    }
                }
            }


            is EventCategory.EditCategory -> {
                viewModelScope.launch {
                    dao.upsert(event.categoryDetails)
                }
            }

            EventCategory.SortCategory -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
        }
    }

}