package com.rarnu.mdpro3.api

import com.isyscore.kotlin.ktor.Result
import com.rarnu.mdpro3.database.Omega
import com.rarnu.mdpro3.request.YdkFindReq
import com.rarnu.mdpro3.request.YdkNamesReq
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ydkApi() = route("/ydk") {

    post<YdkFindReq>("/find") { req ->
        if (req.key.isBlank()) {
            call.respond(HttpStatusCode.InternalServerError, Result.errorNoData())
            return@post
        }
        val data = Omega.ydkFindCardNameList(req)
        call.respond(Result.success(data = data))
    }

    post<YdkNamesReq>("/names") { req ->
        val data = Omega.ydkNamesByIds(req)
        call.respond(Result.success(data = data))
    }

}