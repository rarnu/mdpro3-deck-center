@file:Suppress("DuplicatedCode")

package com.rarnu.mdpro3.api

import com.isyscore.kotlin.ktor.Result
import com.rarnu.mdpro3.database.NameAPI
import com.rarnu.mdpro3.database.Omega
import com.rarnu.mdpro3.database.RushDuel
import com.rarnu.mdpro3.jp.removeKana
import com.rarnu.mdpro3.request.KKNameReq
import com.rarnu.mdpro3.request.YdkFindReq
import com.rarnu.mdpro3.request.YdkNamesReq
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.rushDuelAPI() = route("/rushduel") {

    post<YdkFindReq>("/rdkfind") { req ->
        if (req.key.isBlank()) {
            call.respond(HttpStatusCode.InternalServerError, Result.errorNoData())
            return@post
        }
        val data = RushDuel.rdkFindCardNameList(req)
        call.respond(Result.success(data = data))
    }

    post<YdkNamesReq>("/rdknames") { req ->
        val data = RushDuel.rdkNamesByIds(req)
        call.respond(Result.success(data = data))
    }

    post<KKNameReq>("/kkname") { req ->
        if (req.name.isBlank()) {
            call.respond(HttpStatusCode.InternalServerError, Result.errorNoData())
            return@post
        }
        val name = NameAPI.nameKanjiKana(removeKana(req.name))
        call.respond(Result.success(message = if (name.isBlank()) "not found" else "found", data = name))
    }

    post<KKNameReq>("/kkeffect") { req ->
        if (req.name.isBlank()) {
            call.respond(HttpStatusCode.InternalServerError, Result.errorNoData())
            return@post
        }
        val name = NameAPI.effectKanjiKana(removeKana(req.name))
        call.respond(Result.success(message = if (name.isBlank()) "not found" else "found", data = name))
    }

    get("/list") {
        val name = call.request.queryParameters["name"]
        if (name.isNullOrBlank()) {
            call.respond(Result.successNoData())
            return@get
        }
        val lang = call.request.queryParameters["lang"] ?: "jp"
        val data = RushDuel.rushCardNameList(name, lang)
        call.respond(Result.success(data = data))
    }

    get("/card/{password}") {
        val pass = call.parameters["password"]?.toLongOrNull() ?: 0L
        val lang = call.request.queryParameters["lang"] ?: "jp"
        val ret = Omega.one(pass, lang)
        call.respond(Result.success(data = ret))
    }

    get("/random") {
        val lang = call.request.queryParameters["lang"] ?: "jp"
        val data = RushDuel.random(lang)
        call.respond(Result.success(data = data))
    }
}