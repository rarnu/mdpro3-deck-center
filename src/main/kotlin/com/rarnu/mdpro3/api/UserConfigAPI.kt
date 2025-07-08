package com.rarnu.mdpro3.api

import com.isyscore.kotlin.common.save
import com.rarnu.mdpro3.database.DatabaseManager.dbMDPro3
import com.rarnu.mdpro3.database.table.userConfigs
import com.rarnu.mdpro3.ext.validateSource
import com.rarnu.mdpro3.ext.validateToken
import com.rarnu.mdpro3.ext.validateUserId
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import com.isyscore.kotlin.ktor.KResult
import com.rarnu.mdpro3.database.table.UserConfig

fun Route.userConfigApi() = route("/user/config") {

    /**
     * 获取指定用户的配置
     */
    get("/{userId}") {
        call.validateSource() ?: return@get
        val userId = call.validateUserId() ?: return@get
        call.validateToken(userId) ?: return@get
        val config = dbMDPro3.userConfigs.find { it.userId eq userId }
        call.respond(KResult.success(data = config))
    }

    /**
     * 写入指定用户的配置
     */
    post<UserConfig> { req ->
        val userId = req.userId
        call.validateToken(userId) ?: return@post
        val count = dbMDPro3.userConfigs.save(true, req) { c -> c.userId eq req.userId }
        call.respond(KResult.successNoData(message = "${count > 0}"))
    }

}