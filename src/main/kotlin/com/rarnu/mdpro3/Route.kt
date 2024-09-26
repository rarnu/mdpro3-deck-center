package com.rarnu.mdpro3

import com.rarnu.mdpro3.api.*
import io.ktor.server.routing.*

fun Route.baseApi() = route("/api") {
    // MDPro3 卡组同步
    route("/mdpro3") {
        healthAPI()
        deckAPI()
        syncAPI()
        sensitiveWordAPI()
        userConfigApi()
    }
    // 制卡器
    yugiohApi()
    ydkApi()
    rushDuelAPI()
    kanjikanaAPI()
    commonApi()
}