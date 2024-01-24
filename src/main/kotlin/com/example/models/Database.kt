package com.example.models

import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
    fun init() {
        val dotenv = dotenv()

        val dbUser = dotenv.get("DATABASE_USER")
        val dbPass = dotenv.get("DATABASE_PASSWORD")

        val driverClassName = "com.mysql.cj.jdbc.Driver"
        val jdbcURL = "jdbc:mysql://localhost:3306/posto"

        val database = Database.connect(jdbcURL, driver = driverClassName, user = dbUser, password = dbPass)
        transaction(database) {
            SchemaUtils.create(Transactions)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}