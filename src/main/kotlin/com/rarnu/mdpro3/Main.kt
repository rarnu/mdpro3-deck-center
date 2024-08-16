package com.rarnu.mdpro3

import com.isyscore.kotlin.common.LOCAL_DATETIME_PATTERN
import com.isyscore.kotlin.common.LOCAL_DATE_PATTERN
import com.isyscore.kotlin.ktor.pluginCORS
import com.isyscore.kotlin.ktor.pluginCompress
import com.isyscore.kotlin.ktor.pluginContentNegotiation
import com.isyscore.kotlin.ktor.pluginPartialContent
import com.rarnu.mdpro3.database.DatabaseManager
import com.rarnu.mdpro3.database.DatabaseManager.readDatabaseMDPro3Config
import com.rarnu.mdpro3.database.DatabaseManager.readDatabaseNameAPIConfig
import com.rarnu.mdpro3.database.DatabaseManager.readDatabaseOmegaConfig
import com.rarnu.mdpro3.database.DatabaseManager.readDatabaseRushDuelJPConfig
import com.rarnu.mdpro3.define.AppVersion
import com.rarnu.mdpro3.jp.initKanjikanaData
import com.rarnu.mdpro3.util.Translate.initTranslateData
import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.routing.*
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.seconds

fun main(args: Array<String>) {
    // 全局日期解析
    LOCAL_DATETIME_PATTERN = DateTimeFormatter.ISO_DATE_TIME
    LOCAL_DATE_PATTERN = DateTimeFormatter.ISO_DATE

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    AppVersion = environment.config.propertyOrNull("ktor.application.version")?.getString() ?: "unknown"
    // 初始化数据库
    DatabaseManager.initMDPro3(readDatabaseMDPro3Config())
    DatabaseManager.initNameAPI(readDatabaseNameAPIConfig())
    DatabaseManager.initOmega(readDatabaseOmegaConfig())

    initKanjikanaData()
    initTranslateData()

    // 初始化插件
    pluginCORS()
    pluginCompress()
    pluginPartialContent()
    pluginContentNegotiation(localDateTimePattern = DateTimeFormatter.ISO_DATE_TIME)

    install(RateLimit) {
        global {
            rateLimiter(limit = 50, refillPeriod = 1.seconds)
        }
    }

    routing {
        baseApi()
    }

}
