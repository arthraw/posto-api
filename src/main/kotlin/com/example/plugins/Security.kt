package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureSecurity() {
    data class UserSession(val count: Int = 0)
    install(Sessions) {
        cookie<UserSession>("USER_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    routing {
        get("/session/increment") {
            val session = call.sessions.get<UserSession>() ?: UserSession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}
