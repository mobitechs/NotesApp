package com.mobitechs.notesapp.data_layer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseDetailsDao {
    @Upsert
    suspend fun upsert(expenseDetails: ExpenseDetails)

    @Delete
    suspend fun delete(expenseDetails: ExpenseDetails)

    @Query("SELECT e.*, c.categoryName FROM expenseDetails e INNER JOIN category c ON e.categoryId = c.categoryId ORDER BY e.dateDated ASC")
    fun getAllExpenseDetails(): Flow<List<ExpenseDetails>>

    @Query(" SELECT  e.*, c.categoryName FROM expenseDetails e  INNER JOIN category c ON e.categoryId = c.categoryId ORDER BY e.amount ASC")
//    @Query("Select * from expenseDetails order by amount asc")
    fun getAllExpenseAmountWise(): Flow<List<ExpenseDetails>>

    @Query("select * from category order by dateDated asc")
    fun getCategoryList(): Flow<List<Category>>
}