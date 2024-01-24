package com.example.routesTest

import com.example.FakeModel.FakeTransaction
import com.example.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import java.time.LocalDate

class RoutesTest {
    @Test
    fun `Get all transactions, return all transactions`() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) {
                json()
            }
        }

        val responseUser = FakeTransaction(
            id = 1,
            transactionTypeId = 1,
            cost = 80.00,
            gas = 45.00,
            timestamp = LocalDate.now(),
        )

        client.get("/Transactions") {
            contentType(ContentType.Application.Json)
            val serializedBody = Json.encodeToString(responseUser)

            setBody(serializedBody)
        }

    }
}