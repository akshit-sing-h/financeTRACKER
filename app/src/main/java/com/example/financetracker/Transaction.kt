package com.example.financetracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,  // Automatically generate an ID
    val amount: Double,
    val date: String,
    val category: String
)

