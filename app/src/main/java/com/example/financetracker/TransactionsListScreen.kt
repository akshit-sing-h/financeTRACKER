package com.example.financetracker


import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TransactionsListScreen(navController: NavController, viewModel: DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val transactions by viewModel.allTransactions.observeAsState(emptyList())



    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "All Transactions", style = MaterialTheme.typography.headlineSmall)

        // 🔹 Show message if no transactions exist
        if (transactions.isEmpty()) {
            Text(text = "No transactions available.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(transactions) { transaction ->
                    TransactionCard(transaction)
                }
            }
        }
    }
}


@Composable
fun TransactionCard(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Amount: ₹${transaction.amount}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Date: ${transaction.date}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Category: ${transaction.category}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


