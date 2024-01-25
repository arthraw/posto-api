package com.example.models.dao

import com.example.models.Transaction
import java.time.LocalDate

interface TransactionDAOFacade {
    suspend fun getAllTransactions() : List<Transaction>
    suspend fun getTransactionById(id : Int) : List<Transaction?>
    suspend fun insertTransaction(transactionType: Int, transaction: String, cost: Double, gas : Double, timestamp: LocalDate)
    suspend fun updateTransaction(transactionId : Int, transactionType: Int)
    suspend fun deleteTransaction(id: Int)
}