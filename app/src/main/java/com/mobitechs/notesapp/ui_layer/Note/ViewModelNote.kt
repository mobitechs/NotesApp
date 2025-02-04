package com.mobitechs.notesapp.ui_layer.Note

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobitechs.notesapp.data_layer.Note
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
class ViewModelNote @Inject constructor(noteDatabase: NoteDatabase) : ViewModel() {

    val dao = noteDatabase.noteDao
    private val isSortedByDateAdded = MutableStateFlow(true)
    private var notes = isSortedByDateAdded.flatMapLatest {
        if (it) {
            dao.getAllNoteOrderedDateAdded()
        } else {
            dao.getAllNoteOrderedTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(StateNote())
    val state = combine(_state, isSortedByDateAdded, notes) { state, isSortedByDateAdded, notes ->
        state.copy(notes = notes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StateNote())

    fun onEvent(event: EventNote) {
        when (event) {
            is EventNote.DeleteNote -> {
                viewModelScope.launch {
                    dao.delete(event.note)
                }
            }

            is EventNote.SaveNote -> {
                val note = Note(
                    title = state.value.title.value,
                    disp = state.value.disp.value,
                    dateDated = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    dao.upsert(note)
                    _state.update {
                        it.copy(title = mutableStateOf(""), disp = mutableStateOf(""))
                    }
                }
            }

            EventNote.SortNote -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
        }
    }

}