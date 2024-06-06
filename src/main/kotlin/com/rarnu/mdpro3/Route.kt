package com.rarnu.mdpro3

import com.rarnu.mdpro3.api.deckAPI
import com.rarnu.mdpro3.api.healthAPI
import com.rarnu.mdpro3.api.sensitiveWordAPI
import io.ktor.server.routing.*

fun Route.baseApi() = route("/api/mdpro3") {
    healthAPI()
    deckAPI()
    sensitiveWordAPI()
}