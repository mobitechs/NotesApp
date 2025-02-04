package com.mobitechs.notesapp.data_layer

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity
@Serializable
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val disp: String,
    val dateDated: Long
)

@Entity
@Serializable
data class ExpenseDetails(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: String,
    val categoryName: String?,
    val amount: String,
    val reason: String,
    val date: String,
    val month: String,
    val year: Int,
    val dateDated: Long
)

@Entity
@Serializable
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0,
    val categoryName: String,
    val categoryColorR: String,
    val categoryColorG: String,
    val categoryColorB: String,
    val dateDated: Long
)

@Entity
@Serializable
data class Income(
    @PrimaryKey(autoGenerate = true) val incomeId: Int = 0,
    val incomeMonth: String,
    val incomeYear: String,
    val incomeAmt: String,
    val dateDated: Long
)