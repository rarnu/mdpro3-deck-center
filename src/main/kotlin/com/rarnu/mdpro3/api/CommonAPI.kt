package com.rarnu.mdpro3.api

import com.rarnu.mdpro3.request.TranslateReq
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.isyscore.kotlin.ktor.Result
import com.rarnu.mdpro3.database.NameAPI
import com.rarnu.mdpro3.util.Translate

fun Route.commonApi() = route("/common") {

    post<TranslateReq>("/translate") { req ->
        if (req.query.isBlank()) {
            call.respond(Result.success(data = ""))
            return@post
        }
        val retText = Translate.translate(req.query)
        // 翻译得到空结果
        if (retText.isBlank()) {
            call.respond(Result.success(data = ""))
            return@post
        }
        if (!req.kk) {
            call.respond(Result.success(data = retText))
            return@post
        }
        val retTextKK = if (req.kkMode == "normal") {
            NameAPI.normalKanjiKana(retText)
        } else {
            NameAPI.effectKanjiKana(retText)
        }
        if (retTextKK.isNotBlank()) {
            call.respond(Result.success(data = retTextKK))
        } else {
            call.respond(Result.success(data = retText))
        }
    }
}