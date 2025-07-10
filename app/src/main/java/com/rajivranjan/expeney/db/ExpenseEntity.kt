package com.rajivranjan.expeney.db

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val amount: Double

)