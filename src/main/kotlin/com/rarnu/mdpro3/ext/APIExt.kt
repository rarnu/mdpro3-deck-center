package com.rarnu.mdpro3.ext

import com.isyscore.kotlin.ktor.Result
import com.rarnu.mdpro3.api.validateWord
import com.rarnu.mdpro3.cache.CacheManager
import com.rarnu.mdpro3.database.entity.Deck
import com.rarnu.mdpro3.response.ResultWithValue
import com.rarnu.mdpro3.define.*
import com.rarnu.mdpro3.util.MCTokenValidation
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

/**
 * 验证卡组信息是否正确
 */
suspend fun ApplicationCall.validateDeck(deck: Deck, needId: Boolean = false): Boolean? {
    if (needId) {
        if (deck.deckId.isBlank()) {
            respond(Result.errorNoData(code = ERR_NO_DECK_ID.first, message = ERR_NO_DECK_ID.second))
            return null
        }
    }
    if (deck.deckContributor.isBlank()) {
        respond(Result.errorNoData(code = ERR_NO_CONTRIBUTOR.first, message = ERR_NO_CONTRIBUTOR.second))
        return null
    }
    if (deck.deckName.isBlank()) {
        respond(Result.errorNoData(code = ERR_NO_DECK_NAME.first, message = ERR_NO_DECK_NAME.second))
        return null
    }
    // 敏感词判断，这里把卡组名称和贡献者名称拼在一起查，主要是为了减少查库次数
    val sw = validateWord("${deck.deckName},${deck.deckContributor}")
    if (sw.isNotEmpty()) {
        respond(ResultWithValue.errorNoData(code = ERR_NAME_IS_SENSITIVE.first, message = ERR_NAME_IS_SENSITIVE.second, messageValue = sw.joinToString(",")))
        return null
    }
    if (deck.deckYdk.isNullOrBlank()) {
        respond(Result.errorNoData(code = ERR_NO_YDK.first, message = ERR_NO_YDK.second))
        return null
    }

    if (deck.deckYdk?.replace("\r", "")?.split("\n")?.isValidYdk() != true) {
        respond(Result.errorNoData(code = ERR_YDK_NOT_VALID.first, message = ERR_YDK_NOT_VALID.second))
        return null
    }
    return true
}

/**
 * 验证用户ID是否正确传入
 */
suspend fun ApplicationCall.validateUserId(): Long? {
    val userId = parameters["userId"]?.toLongOrNull()
    if (userId == null) {
        // application.log.error("请求 [${request.path()}] 没有提供 userId 参数")
        respond(Result.errorNoData(code = ERR_NO_USER_ID.first, message = ERR_NO_USER_ID.second))
        return null
    }
    return userId
}

suspend fun ApplicationCall.validateReqUserId(userId: Long): Boolean? {
    if (userId == 0L) {
        respond(Result.errorNoData(code = ERR_NO_USER_ID.first, message = ERR_NO_USER_ID.second))
        return null
    }
    return true
}

suspend fun ApplicationCall.validateId(): String? {
    val id = parameters["id"]
    if (id.isNullOrBlank()) {
        respond(Result.errorNoData(code = ERR_NO_DECK_ID.first, message = ERR_NO_DECK_ID.second))
        return null
    }
    return id
}

suspend fun ApplicationCall.validateSource(): Boolean? {
    val src = request.header("ReqSource")
    if (src.isNullOrBlank() || src != "MDPro3") {
        // application.log.error("请求 [${request.path()}] 没有提供合法的 ReqSource")
        respond(Result.errorNoData(code = ERR_REQ_SOURCE.first, message = ERR_REQ_SOURCE.second))
        return null
    }
    return true
}

suspend fun ApplicationCall.validateDauSrc(): String? {
    val src = request.queryParameters["src"]
    if (src.isNullOrBlank()) {
        respond(Result.errorNoData(code = ERR_DAU_SRC.first, message = ERR_DAU_SRC.second))
        return null
    }
    return src
}

suspend fun ApplicationCall.validateDauYear(): Int? {
    val year = request.queryParameters["year"]?.toIntOrNull()
    if (year == null) {
        respond(Result.errorNoData(code = ERR_DAU_YEAR.first, message = ERR_DAU_YEAR.second))
        return null
    }
    return year
}

suspend fun ApplicationCall.validateDauMonth(): Int? {
    val month = request.queryParameters["month"]?.toIntOrNull()
    if (month == null) {
        respond(Result.errorNoData(code = ERR_DAU_MONTH.first, message = ERR_DAU_MONTH.second))
        return null
    }
    return month
}

/**
 * 验证用户的 token
 */
suspend fun ApplicationCall.validateToken(userId: Long): Boolean? {
    val token = request.header("token")
    if (token.isNullOrBlank()) {
        // application.log.error("请求 [${request.path()}] 没有提供 token")
        respond(Result.errorNoData(code = ERR_NO_TOKEN.first, message = ERR_NO_TOKEN.second))
        return null
    }
    val cacheKey = "mc_${userId}_$token"
    return CacheManager.getMcValidated(cacheKey) {
        MCTokenValidation.validate(token, userId)
    }
}

fun List<String>.isValidYdk(): Boolean {
    var count = 0
    var inMain = false
    for (s in this) {
        if (s == "#main") {
            inMain = true
            continue
        }
        if (s == "#extra") break
        if (inMain) count++
    }
    return count >= 40
}