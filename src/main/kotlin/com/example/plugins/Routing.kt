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
//        route("/Transactions") {
//
//            get("/Transactions") {
//                val transactionsList = dao.getAllTransactions()
//                call.respond(transactionsList)
//            }
//            get("/Transactions/{id}") {
//                val idParam = call.parameters["id"] ?: ""
//                dao.getTransactionById(idParam.toInt())
//            }
//            post("/Transactions") {
//                val receive = call.receive<TransactionInsert>()
//                dao.insertTransaction(
//                    receive.transactionTypeId,
//                    receive.transaction,
//                    receive.cost,
//                    receive.gas,
//                    receive.timestamp
//                )
//                call.respondText("Transaction ok", status = HttpStatusCode.Created)
//            }
//        }
        get("/Transactions") {
            val transactionsList = dao.getAllTransactions()
            call.respond(transactionsList)
        }
        get("/Transactions/{id}") {
            val idParam = call.parameters["id"]?.toInt()
            try {
                if (idParam != null) {
                    dao.getTransactionById(idParam).let {
                        call.respond(it)
                    }
                } else {
                    call.respondText("No return", status = HttpStatusCode.NotFound)
                }
            } catch (e : InvalidParameterException) {
                call.respondText("Error in server", status = HttpStatusCode.BadGateway)
            }
        }
        post("/Transactions") {
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
    }
}

