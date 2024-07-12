package com.rarnu.mdpro3

import com.isyscore.kotlin.common.LOCAL_DATETIME_PATTERN
import com.isyscore.kotlin.common.LOCAL_DATE_PATTERN
import com.isyscore.kotlin.ktor.pluginCORS
import com.isyscore.kotlin.ktor.pluginCompress
import com.isyscore.kotlin.ktor.pluginContentNegotiation
import com.isyscore.kotlin.ktor.pluginPartialContent
import com.rarnu.mdpro3.database.DatabaseManager
import com.rarnu.mdpro3.database.DatabaseManager.readDatabaseConfig
import com.rarnu.mdpro3.define.AppVersion
import io.ktor.server.application.*
import io.ktor.server.routing.*
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    // 全局日期解析
    LOCAL_DATETIME_PATTERN = DateTimeFormatter.ISO_DATE_TIME
    LOCAL_DATE_PATTERN = DateTimeFormatter.ISO_DATE

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    AppVersion = environment.config.propertyOrNull("ktor.application.version")?.getString() ?: "unknown"

    val (jdbcUrl, user, password) = readDatabaseConfig()
    DatabaseManager.init(jdbcUrl, user, password)

    pluginCORS()
    pluginCompress()
    pluginPartialContent()
    pluginContentNegotiation(localDateTimePattern = DateTimeFormatter.ISO_DATE_TIME)

    routing {
        baseApi()
    }

}
