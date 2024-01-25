package com.example.plugins

import com.example.models.TransactionInsert
import com.example.service.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.security.InvalidParameterException


fun Application.configureRouting() {
    install(Resources)
    routing {
        get("/") {
            call.respondText("Not found")
        }
        route("/Transactions") {
            get {
                val transactionsList = dao.getAllTransactions()
                call.respond(transactionsList)
            }
            get("/{id}") {
                val idParam = call.parameters["id"]?.toInt()
                try {
                    if (idParam != null) {
                        dao.getTransactionById(idParam).let {
                            call.respond(it)
                        }
                    } else {
                        call.respondText("No return", status = HttpStatusCode.NotFound)
                    }
                } catch (e: InvalidParameterException) {
                    call.respondText("Error in server", status = HttpStatusCode.BadGateway)
                }
            }
            post {
                val receive = call.receive<TransactionInsert>()
                dao.insertTransaction(
                    receive.transactionTypeId,
                    receive.transaction,
                    receive.cost,
                    receive.gas,
                    receive.timestamp
                )
                call.respondText("Transaction ok", status = HttpStatusCode.Created)
            }
            patch("/{id}") {
                val idParam = call.parameters["id"] ?: ""
                val transactionName = call.request.queryParameters["transaction"] ?: ""
                dao.getTransactionById(idParam.toInt()).let {
                    if (it.isEmpty()) {
                        call.respondText(
                            "Transaction could not be updated, check if this transaction id exist`s",
                            status = HttpStatusCode.NotFound
                        )
                    }
                    dao.updateTransaction(transactionId = idParam.toInt(), transactionName)
                    call.respondText("Transaction updated", status = HttpStatusCode.OK)
                }
            }
            delete("/{id}") {
                val idParam = call.parameters["id"] ?: ""
                dao.getTransactionById(idParam.toInt()).let {
                    if (it.isEmpty()) {
                        call.respondText(
                            "Transaction could not be deleted, check if this transaction id exist`s",
                            status = HttpStatusCode.NotFound
                        )
                    }
                    dao.deleteTransaction(idParam.toInt())
                    call.respondText("Transaction deleted", status = HttpStatusCode.OK)
                }
            }
        }
    }
}

