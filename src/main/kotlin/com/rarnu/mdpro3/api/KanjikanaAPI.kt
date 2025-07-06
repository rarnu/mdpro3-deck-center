package com.rarnu.mdpro3.api

import com.rarnu.mdpro3.request.KKNameReq
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.isyscore.kotlin.ktor.KResult
import com.rarnu.mdpro3.database.NameAPI
import com.rarnu.mdpro3.jp.removeKana

fun Route.kanjikanaAPI() = route("/kanjikana") {

    post<KKNameReq>("/name") { req ->
        call.validateName(req) ?: return@post
        val name = NameAPI.nameKanjiKana(removeKana(req.name))
        call.respond(KResult.success(message = if (name.isBlank()) "not found" else "found", data = name))
    }


    post<KKNameReq>("/effect") { req ->
        call.validateName(req) ?: return@post
        val name = NameAPI.effectKanjiKana(removeKana(req.name))
        call.respond(KResult.success(message = if (name.isBlank()) "not found" else "found", data = name))
    }

    post<KKNameReq>("/text") { req ->
        call.validateName(req) ?: return@post
        val name = NameAPI.normalKanjiKana(removeKana(req.name))
        call.respond(KResult.success(message = if (name.isBlank()) "not found" else "found", data = name))
    }
}

private suspend fun ApplicationCall.validateName(req: KKNameReq): Boolean? {
    if (req.name.isBlank()) {
        respond(HttpStatusCode.InternalServerError, KResult.errorNoData())
        return null
    }
    return true
}