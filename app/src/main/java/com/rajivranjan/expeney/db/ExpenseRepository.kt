package com.rajivranjan.expeney.db

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {

    val allExpenses: Flow<List<ExpenseEntity>> = dao.getAllExpenses()

    suspend fun insertExpense(expense: ExpenseEntity) {
        dao.insertExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        dao.deleteExpense(expense)
    }
}
