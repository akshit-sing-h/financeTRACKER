package com.example.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.ui.theme.FinanceTRACKERTheme
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


@Composable
fun DashboardScreen(navController: NavController, viewModel: DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val transactions by viewModel.allTransactions.observeAsState(initial = emptyList())

    // Track whether fields are empty
    val isAmountEmpty = remember { mutableStateOf(false) }
    val isDateEmpty = remember { mutableStateOf(false) }
    val isCategoryEmpty = remember { mutableStateOf(false) }

    // Track the selected category
    val selectedCategory = remember { mutableStateOf("") }

    // Show dialog if any field is empty
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        // Displaying the AlertDialog for "Please fill all fields"
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Error") },
            text = { Text(text = "Please fill all fields.") },
            confirmButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text("OK")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(text = "Welcome back, Akshit", style = MaterialTheme.typography.headlineSmall)
        }
        item {
            Text(text = "Manage your expenses", style = MaterialTheme.typography.titleLarge)
        }

        // Input Fields (Amount, Date, Category)
        item {
            Text(text = "Enter Amount", style = MaterialTheme.typography.bodyLarge)
        }
        item {
            OutlinedTextField(
                value = viewModel.amount.value,
                onValueChange = {
                    viewModel.updateAmount(it)
                    isAmountEmpty.value = it.isEmpty()  // Check if amount is empty
                },
                label = { Text(text = "Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isAmountEmpty.value  // Show error if empty
            )
        }

        item {
            Text(text = "Enter Date", style = MaterialTheme.typography.bodyLarge)
        }
        item {
            OutlinedTextField(
                value = viewModel.date.value,
                onValueChange = {
                    viewModel.updateDate(it)
                    isDateEmpty.value = it.isEmpty()  // Check if date is empty
                },
                label = { Text(text = "Date") },
                isError = isDateEmpty.value  // Show error if empty
            )
        }

        item {
            Text(text = "Select Category", style = MaterialTheme.typography.bodyLarge)
        }
        item {
            val expanded = remember { mutableStateOf(false) }
            val categoryOptions = listOf("Food", "Hobbies", "Groceries", "Activity", "Miscellaneous", "Others", "Bills")

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expanded.value = !expanded.value },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors()
                ) {
                    Text(
                        text = if (selectedCategory.value.isEmpty()) "Nothing Selected" else selectedCategory.value
                    )
                }
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                ) {
                    categoryOptions.forEach { category ->
                        DropdownMenuItem(
                            onClick = {
                                selectedCategory.value = category
                                expanded.value = false
                                viewModel.updateCategory(category)
                                isCategoryEmpty.value = false  // Reset error state
                            },
                            text = { Text(text = category) }
                        )
                    }
                }
            }
        }

        // Save Transaction Button
        item {
            Button(
                onClick = {
                    // Show dialog if any field is empty
                    if (viewModel.amount.value.isEmpty() || viewModel.date.value.isEmpty() || selectedCategory.value.isEmpty()) {
                        showDialog.value = true
                    } else {
                        viewModel.saveTransaction()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
            ) {
                Text(text = "Save Transaction", color = Color.White)
            }
        }

        // Button to navigate to Transactions List
        item {
            Button(
                onClick = { navController.navigate("transactionsList") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
            ) {
                Text(text = "View All Transactions", color = Color.White)
            }
        }

        item {
            PieChartSection(transactions)
        }
    }
}

@Composable
fun PieChartSection(transactions: List<Transaction>) {
    val categoryTotals = transactions.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }

    // Create a list of PieEntry objects
    val pieEntries = categoryTotals.map { (category, total) ->
        PieEntry(total.toFloat(), category)  // The total amount for each category
    }

    // Create the PieChart with the prepared data
    PieChartView(pieEntries)
}

@Composable
fun PieChartView(entries: List<PieEntry>) {
    val context = LocalContext.current

    AndroidView(
        factory = { PieChart(context) },
        update = { pieChart ->
            val dataSet = PieDataSet(entries, "Transaction Categories").apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList()
                valueTextSize = 16f
                valueTextColor = Color.White.toArgb()
            }

            val pieData = PieData(dataSet)
            pieChart.data = pieData

            pieChart.apply {
                description.isEnabled = false
                isRotationEnabled = true
                setUsePercentValues(true)
            }

            pieChart.invalidate()  // Refresh chart
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

