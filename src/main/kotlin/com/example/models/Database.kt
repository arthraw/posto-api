package com.example.models

import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
    fun init() {
        // Search env variables
        val dotenv = dotenv {
            directory = "src/.env"
        }
        // Get env variables
        val dbUser = dotenv.get("DATABASE_USER")
        val dbPass = dotenv.get("DATABASE_PASSWORD")

        // Flyway configuration
        val flyway = Flyway.configure()
            .dataSource("jdbc:mysql://localhost:3306/posto", dbUser, dbPass)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .load()

        flyway.migrate()

        val driverClassName = "com.mysql.cj.jdbc.Driver"
        val jdbcURL = "jdbc:mysql://localhost:3306/posto"

        Database.connect(jdbcURL, driver = driverClassName, user = dbUser, password = dbPass)

        transaction {
            SchemaUtils.create(Transactions)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}