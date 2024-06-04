package com.rarnu.mdpro3.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.isyscore.kotlin.ktor.Result
import com.rarnu.mdpro3.define.AppVersion

fun Route.healthAPI() = route("/health") {

    get("/status") {
        call.respond(Result.success(data = "OK"))
    }

    get("/version") {
        call.respond(Result.success(data = AppVersion))
    }

}