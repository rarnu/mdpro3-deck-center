package com.rarnu.mdpro3

import com.isyscore.kotlin.common.LOCAL_DATETIME_PATTERN
import com.isyscore.kotlin.common.LOCAL_DATE_PATTERN
import com.isyscore.kotlin.ktor.KResult
import com.isyscore.kotlin.ktor.errorRespond
import com.isyscore.kotlin.ktor.pluginCORS
import com.isyscore.kotlin.ktor.pluginCompress
import com.isyscore.kotlin.ktor.pluginContentNegotiation
import com.isyscore.kotlin.ktor.pluginPartialContent
import com.rarnu.mdpro3.database.DatabaseManager
import com.rarnu.mdpro3.database.DatabaseManager.readDatabaseMDPro3Config
import com.rarnu.mdpro3.database.DatabaseManager.readDatabaseNameAPIConfig
import com.rarnu.mdpro3.database.DatabaseManager.readDatabaseOmegaConfig
import com.rarnu.mdpro3.define.AppVersion
import com.rarnu.mdpro3.define.HTTP_ERR_BAD_REQUEST
import com.rarnu.mdpro3.define.HTTP_ERR_METHOD_NOT_ALLOWED
import com.rarnu.mdpro3.define.HTTP_ERR_NOT_FOUND
import com.rarnu.mdpro3.define.HTTP_ERR_RATE_LIMIT_EXCEEDED
import com.rarnu.mdpro3.define.HTTP_ERR_REQUEST_TIMEOUT
import com.rarnu.mdpro3.define.HTTP_ERR_SERVER
import com.rarnu.mdpro3.jp.initKanjikanaData
import com.rarnu.mdpro3.util.SnowFlakeManager.initSnowFlake
import com.rarnu.mdpro3.util.Translate.initTranslateData
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
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
    // 初始化数据库
    DatabaseManager.initMDPro3(readDatabaseMDPro3Config())
    DatabaseManager.initNameAPI(readDatabaseNameAPIConfig())
    DatabaseManager.initOmega(readDatabaseOmegaConfig())

    initSnowFlake()
    initKanjikanaData()
    initTranslateData()

    // 初始化插件
    pluginCORS()
    pluginCompress()
    pluginPartialContent()
    pluginContentNegotiation(localDateTimePattern = DateTimeFormatter.ISO_DATE_TIME)

    // 全局异常处理
    install(StatusPages) {

        status(HttpStatusCode.BadRequest) { call, _ ->
            call.errorRespond(HTTP_ERR_BAD_REQUEST, call.request.path())
        }

        status(HttpStatusCode.NotFound) { call, _ ->
            call.errorRespond(HTTP_ERR_NOT_FOUND, call.request.path())
        }

        status(HttpStatusCode.MethodNotAllowed) { call, _ ->
            call.errorRespond(HTTP_ERR_METHOD_NOT_ALLOWED, call.request.path(), call.request.httpMethod.value)
        }

        status(HttpStatusCode.TooManyRequests) { call, _ ->
            call.errorRespond(HTTP_ERR_RATE_LIMIT_EXCEEDED, call.request.path())
        }

        status(HttpStatusCode.RequestTimeout) { call, _ ->
            call.errorRespond(HTTP_ERR_REQUEST_TIMEOUT, call.request.path())
        }

        exception<Throwable> { call, cause ->
            // 接到全局异常
            call.respond(KResult.errorNoData(code = HTTP_ERR_SERVER.first, message = "$cause"))
        }

    }

    routing {
        baseApi()
    }

}
