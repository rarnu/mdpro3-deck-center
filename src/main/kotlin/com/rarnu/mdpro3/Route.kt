package com.rarnu.mdpro3

import com.rarnu.mdpro3.api.*
import io.ktor.server.routing.*

fun Route.baseApi() = route("/api/mdpro3") {
    healthAPI()
    deckAPI()
    syncAPI()
    sensitiveWordAPI()
    dataAnalysisAPI()
}