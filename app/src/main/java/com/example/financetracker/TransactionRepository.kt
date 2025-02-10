
package com.example.financetracker

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao) {

    // Fetch all transactions as LiveData
    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    // Insert a new transaction
    @WorkerThread
    suspend fun insert(transaction: Transaction) {
        transactionDao.insertTransaction(transaction) // Corrected method name
    }

    // Add method to get transaction by ID
    suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)
    }
}

