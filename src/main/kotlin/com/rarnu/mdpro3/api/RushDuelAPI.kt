package com.rarnu.mdpro3.api

import com.rarnu.mdpro3.request.YdkFindReq
import io.ktor.server.routing.*

fun Route.rushDuelAPI() = route("/rushduel") {

    post<YdkFindReq>("/rdkfind") {

    }

    post("/rdknames") {

    }

    post("/kkname") {

    }

    post("/kkeffect") {

    }

    get("/list") {

    }

    get("/card/{password}") {

    }

    get("/random") {

    }
}