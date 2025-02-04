package com.mobitechs.notesapp.data_layer


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface IncomeDao {
    @Upsert
    suspend fun upsert(incomeDao: Income)

    @Delete
    suspend fun delete(incomeDao: Income)

    @Query("select * from income order by dateDated asc")
    fun getAllIncome(): Flow<List<Income>>

    @Query("select * from income order by incomeAmt asc")
    fun getAllIncomeByIncomeAmt(): Flow<List<Income>>


}