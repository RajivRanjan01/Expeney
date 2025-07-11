package com.rajivranjan.expeney.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rajivranjan.expeney.db.ExpenseEntity
import com.rajivranjan.expeney.db.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val expenses: StateFlow<List<ExpenseEntity>> =
        repository.allExpenses
            .map { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun addExpense(name: String, amount: Double, category: String) {
        val newExpense = ExpenseEntity(name = name, amount = amount, category = category)
        viewModelScope.launch {
            repository.insertExpense(newExpense)
        }
    }


    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }
}
