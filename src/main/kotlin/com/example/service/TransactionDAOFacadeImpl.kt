package com.example.service

import com.example.models.DatabaseSingleton.dbQuery
import com.example.models.Transaction
import com.example.models.Transactions
import com.example.models.dao.TransactionDAOFacade
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDate

class TransactionDAOFacadeImpl : TransactionDAOFacade {

    private fun resultRowToTransaction(row: ResultRow) = Transaction(
        id = row[Transactions.id],
        transactionTypeId = row[Transactions.transactionTypesId],
        transaction = row[Transactions.transactionType],
        cost = row[Transactions.cost],
        gas =  row[Transactions.gas],
        timestamp = row[Transactions.timestamp]
    )
    override suspend fun getAllTransactions(): List<Transaction> = dbQuery {
        Transactions.selectAll().map(::resultRowToTransaction)
    }

    override suspend fun getTransactionById(id: Int): List<Transaction?> = dbQuery {
        Transactions
            .select(Transactions.id eq id)
            .map(::resultRowToTransaction)
    }

    override suspend fun insertTransaction(
        transactionType: Int,
        transaction: String,
        cost: Double,
        gas: Double,
        timestamp: LocalDate
    ): Unit = dbQuery {
        val insertData  = Transactions.insert {
            it[transactionTypesId] = transactionType
            it[Transactions.transactionType] = transaction
            it[Transactions.cost] = cost
            it[Transactions.gas] = gas
            it[Transactions.timestamp] = timestamp
        }
        insertData.resultedValues?.singleOrNull()?.let(::resultRowToTransaction)
    }

    override suspend fun updateTransaction(transactionId : Int, transaction: String): Unit = dbQuery {
        Transactions.update({ Transactions.id eq transactionId }) {
            it[Transactions.transactionType] = transaction
        }
    }

    override suspend fun deleteTransaction(id: Int): Unit = dbQuery {
        Transactions.deleteWhere{ Transactions.id eq id }
    }

}

val dao: TransactionDAOFacade = TransactionDAOFacadeImpl()