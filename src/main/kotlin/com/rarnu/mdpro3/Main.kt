package com.rarnu.mdpro3

import com.isyscore.kotlin.common.LOCAL_DATETIME_PATTERN
import com.isyscore.kotlin.ktor.pluginCORS
import com.isyscore.kotlin.ktor.pluginCompress
import com.isyscore.kotlin.ktor.pluginContentNegotiation
import com.isyscore.kotlin.ktor.pluginPartialContent
import com.rarnu.mdpro3.database.DatabaseManager
import com.rarnu.mdpro3.database.DatabaseManager.readDatabaseConfig
import com.rarnu.mdpro3.define.AppVersion
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    // 全局日期解析
    LOCAL_DATETIME_PATTERN = DateTimeFormatter.ISO_DATE_TIME

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    AppVersion = environment.config.propertyOrNull("ktor.application.version")?.getString() ?: "unknown"

    val (jdbcUrl, user, password) = readDatabaseConfig()
    DatabaseManager.init(jdbcUrl, user, password)

    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Head)
        allowMethod(HttpMethod.Options)
        allowHeaders { true }
        allowOrigins { true }
        allowCredentials = true
        allowNonSimpleContentTypes = true
        maxAgeInSeconds = 1000L * 60 * 60 * 24
    }

    // pluginCORS()
    pluginCompress()
    pluginPartialContent()
    pluginContentNegotiation(localDatePattern = DateTimeFormatter.ISO_DATE_TIME)

    routing {
        baseApi()
    }

}
