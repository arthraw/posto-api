package com.example.models.dao

import com.example.models.DatabaseSingleton.dbQuery
import com.example.models.Transaction
import com.example.models.Transactions
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDate

class TransactionDAOFacadeImpl : TransactionDAOFacade {

    private fun resultRowToTransaction(row: ResultRow) = Transaction(
        id = row[Transactions.id],
        transactionTypeId = row[Transactions.transactionTypesId],
        cost = row[Transactions.cost],
        gas =  row[Transactions.gas],
        timestamp = row[Transactions.timestamp]
    )
    override suspend fun getAllTransactions(): List<Transaction> = dbQuery {
        Transactions.selectAll().map(::resultRowToTransaction)
    }

    override suspend fun getTransactionById(id: Int): Transaction? = dbQuery {
        Transactions
            .select(Transactions.id eq id)
            .map(::resultRowToTransaction)
            .singleOrNull()
    }

    override suspend fun insertTransaction(
        transactionType: Int,
        cost: Double,
        gas: Double,
        timestamp: LocalDate
    ): Unit = dbQuery {
        val insertData  = Transactions.insert {
            it[transactionTypesId] = transactionType
            it[Transactions.cost] = cost
            it[Transactions.gas] = gas
            it[Transactions.timestamp] = timestamp
        }
        insertData.resultedValues?.singleOrNull()?.let(::resultRowToTransaction)
    }

    override suspend fun updateTransaction(transactionId : Int, transactionType: Int): Unit = dbQuery {
        Transactions.update({ Transactions.id eq transactionId }) {
            it[transactionTypesId] = transactionType
        }
    }

    override suspend fun deleteTransaction(id: Int): Unit = dbQuery {
        Transactions.deleteWhere{ Transactions.id eq id }
    }

}

val dao: TransactionDAOFacade = TransactionDAOFacadeImpl()