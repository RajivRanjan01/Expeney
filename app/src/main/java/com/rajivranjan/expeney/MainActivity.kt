package com.rajivranjan.expeney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rajivranjan.expeney.db.ExpenseDatabase
import com.rajivranjan.expeney.db.ExpenseRepository
import com.rajivranjan.expeney.viewmodel.ExpenseViewModel
import com.rajivranjan.expeney.ui.theme.ExpeneyTheme
import androidx.compose.material3.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val database = remember { ExpenseDatabase.getDatabase(context) }
            val repository = remember { ExpenseRepository(database.expenseDao()) }
            val viewModel: ExpenseViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return ExpenseViewModel(repository) as T
                    }
                }
            )

            ExpeneyTheme {
                ExpenseInputScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpenseInputScreen(viewModel: ExpenseViewModel) {
    val categories = listOf("Food", "Transport", "Shopping", "Bills", "Other")
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    val expenses by viewModel.expenses.collectAsState()
    val totalAmount = expenses.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Track Your Expense 💸",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2196F3)
        )

        Text(
            text = "Total Spent: ₹${"%.2f".format(totalAmount)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF4CAF50),
            modifier = Modifier.padding(top = 8.dp)
        )

        TextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text("Enter expense name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        TextField(
            value = expenseAmount,
            onValueChange = { expenseAmount = it },
            label = { Text("Enter expense amount") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = selectedCategory,
                onValueChange = {},
                label = { Text("Select category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (expenseName.isNotBlank() && expenseAmount.isNotBlank()) {
                    val amount = expenseAmount.toDoubleOrNull()
                    if (amount != null) {
                        viewModel.addExpense(expenseName, amount, selectedCategory)
                        expenseName = ""
                        expenseAmount = ""
                        selectedCategory = categories[0]
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Expense")
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(expenses, key = { it.hashCode() }) { expense ->
                val dismissState = rememberDismissState()

                if (
                    dismissState.isDismissed(DismissDirection.StartToEnd) ||
                    dismissState.isDismissed(DismissDirection.EndToStart)
                ) {
                    viewModel.deleteExpense(expense)
                }

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(
                        DismissDirection.StartToEnd,
                        DismissDirection.EndToStart
                    ),
                    background = {},
                    dismissContent = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "Name: ${expense.name}",
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Amount: ₹${expense.amount}",
                                color = Color(0xFF4CAF50)
                            )
                            Text(
                                text = "Category: ${expense.category}",
                                color = Color.Gray
                            )
                            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseInputScreenPreview() {
    ExpeneyTheme {
        Text("Preview not supported with ViewModel")
    }
}
