package com.rarnu.mdpro3.api

import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.isyscore.kotlin.ktor.KResult
import com.rarnu.mdpro3.define.AppVersion

fun Route.healthAPI() = route("/health") {

    get("/status") {
        call.respond(KResult.success(data = "OK"))
    }

    get("/version") {
        call.respond(KResult.success(data = AppVersion))
    }

}