package com.mobitechs.notesapp.ui_layer.Note

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mobitechs.notesapp.data_layer.Note

data class StateNote(
    val notes: List<Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val disp: MutableState<String> = mutableStateOf(""),
    val addedDate: MutableState<String> = mutableStateOf(""),
)