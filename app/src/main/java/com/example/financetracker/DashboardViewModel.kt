package com.example.financetracker

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TransactionRepository
    val allTransactions: LiveData<List<Transaction>>

    var amount = mutableStateOf("")
        private set

    var date = mutableStateOf("")
        private set

    var category = mutableStateOf("")
        private set

    // Firestore instance
    private val firestore = FirebaseFirestore.getInstance()

    init {
        val dao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(dao)
        allTransactions = repository.allTransactions  // LiveData for Room
    }

    fun updateAmount(newAmount: String) {
        amount.value = newAmount
    }

    fun updateDate(newDate: String) {
        date.value = newDate
    }

    fun updateCategory(newCategory: String) {
        category.value = newCategory
    }

    fun saveTransaction() {
        val transaction = Transaction(
            amount = amount.value.toDoubleOrNull() ?: 0.0,
            date = date.value,
            category = category.value
        )

        // Save to Room Database (Local)
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(transaction)
        }



        // Reset the text fields
        resetFields()
    }



    private fun resetFields() {
        amount.value = ""
        date.value = ""
        category.value = ""
    }
}

