package com.mobitechs.notesapp.data_layer

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    exportSchema = true,
    entities = arrayOf(Note::class, Category::class, ExpenseDetails::class, Income::class),
    version = 1
)
abstract class NoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao
    abstract val expenseDetailsDao: ExpenseDetailsDao
    abstract val categoryDao: CategoryDao
    abstract val incomeDao: IncomeDao
}