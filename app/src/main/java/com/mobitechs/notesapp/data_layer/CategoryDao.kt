package com.mobitechs.notesapp.data_layer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("select * from category order by dateDated asc")
    fun getAllNoteOrderedDateAdded(): Flow<List<Category>>

    @Query("Select * from category order by categoryName asc")
    fun getAllNoteOrderedTitle(): Flow<List<Category>>


}