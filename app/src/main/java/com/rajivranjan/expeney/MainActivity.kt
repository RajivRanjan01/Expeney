package com.rajivranjan.expeney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableStateListOf
import com.rajivranjan.expeney.ui.theme.ExpeneyTheme

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




@Preview(showBackground = true)
@Composable
fun ExpenseInputScreen() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

    ){


        Text(
            text =" Track Your Expense \uD83D\uDCB8",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2196F3)
        )


        var expenseName by remember { mutableStateOf("") }

        var expenseAmount by remember { mutableStateOf("") }

        val expenseList = remember { mutableStateListOf<Expense>() }



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
        Spacer(modifier = Modifier.padding(24.dp))

        Button(onClick = {
            if(expenseName.isNotBlank() && expenseAmount.isNotBlank()) {
                expenseList.add(Expense(expenseName,expenseAmount))
                expenseName = ""
                expenseAmount = ""
            }

        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp))
        { Text(text = "Add Expense") }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            items(expenseList){
                expense ->
                Column { Text(text = "Name: ${expense.name} ")
                    Text(text = "Amount: ₹${expense.amount}") }
            }
        }


    }


    }

data class Expense(
    val name:String,
    val amount:String
)