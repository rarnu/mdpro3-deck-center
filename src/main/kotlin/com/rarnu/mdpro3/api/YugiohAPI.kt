package com.rarnu.mdpro3.api

import com.isyscore.kotlin.ktor.KResult
import com.rarnu.mdpro3.database.Omega
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.yugiohApi() = route("/yugioh") {

    get("/list") {
        val name = call.request.queryParameters["name"]
        if (name.isNullOrBlank()) {
            call.respond(KResult.successNoData())
            return@get
        }
        val lang = call.request.queryParameters["lang"] ?: "jp"
        val ret = Omega.cardNameList(name, lang)
        call.respond(KResult.success(data = ret))
    }

    get("/card/{password}") {
        val pass = call.parameters["password"]?.toLongOrNull() ?: 0L
        val lang = call.request.queryParameters["lang"] ?: "jp"
        val ret = Omega.one(pass, lang)
        if (ret != null) {
            call.respond(ret)
        } else {
            call.respond(KResult.errorNoData())
        }
    }

    get("/random") {
        val lang = call.request.queryParameters["lang"] ?: "jp"
        val data = Omega.random(lang)
        if (data != null) {
            call.respond(data)
        } else {
            call.respond(KResult.errorNoData())
        }
    }

}