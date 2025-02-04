package com.mobitechs.notesapp.data_layer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {
    @Upsert
    suspend fun upsert(notes: Note)

    @Delete
    suspend fun delete(notes: Note)

    @Query("select * from note order by dateDated asc")
    fun getAllNoteOrderedDateAdded(): Flow<List<Note>>

    @Query("Select * from note order by title asc")
    fun getAllNoteOrderedTitle(): Flow<List<Note>>


}