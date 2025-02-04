package com.mobitechs.notesapp.ui_layer.Note

import com.mobitechs.notesapp.data_layer.Note

sealed interface EventNote {

    object SortNote : EventNote
    data class DeleteNote(val note: Note) : EventNote
    data class SaveNote(var title: String, var dips: String) : EventNote

}