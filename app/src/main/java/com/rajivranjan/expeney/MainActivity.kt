package com.rajivranjan.expeney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rajivranjan.expeney.ui.theme.ExpeneyTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.material3.HorizontalDivider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpeneyTheme {
                ExpenseInputScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpenseInputScreen() {
    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    val expenseList = remember { mutableStateListOf<Expense>() }
    val totalAmount = expenseList.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

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

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (expenseName.isNotBlank() && expenseAmount.isNotBlank()) {
                    expenseList.add(Expense(expenseName, expenseAmount))
                    expenseName = ""
                    expenseAmount = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Add Expense")
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(expenseList, key = { it.hashCode() }) { expense ->
                val dismissState = rememberDismissState()
                if (dismissState.isDismissed(DismissDirection.StartToEnd) ||
                    dismissState.isDismissed(DismissDirection.EndToStart)
                ) {
                    expenseList.remove(expense)
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
                            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                        }
                    }
                )
            }
        }
    }
}

data class Expense(
    val name: String,
    val amount: String
)

@Preview(showBackground = true)
@Composable
fun ExpenseInputScreenPreview() {
    ExpeneyTheme {
        ExpenseInputScreen()
    }
}
