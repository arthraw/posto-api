package com.example.plugins

import com.example.models.TransactionInsert
import com.example.models.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Resources)
    routing {
        get("/") {
            call.respondText("Not found")
        }
        get("/Transactions") {
            val transactionsList = dao.getAllTransactions()
            call.respond(transactionsList)
        }
        post("/Transactions") {
            val receive = call.receive<TransactionInsert>()
            dao.insertTransaction(
                receive.transactionTypeId,
                receive.cost,
                receive.gas,
                receive.timestamp
            )
            call.respondText("Transaction ok", status = HttpStatusCode.Created)
        }
    }
}

