@file:Suppress("DuplicatedCode")

package com.rarnu.mdpro3.ext

import com.isyscore.kotlin.ktor.errorRespond
import com.rarnu.mdpro3.api.validateWord
import com.rarnu.mdpro3.cache.CacheManager
import com.rarnu.mdpro3.database.DatabaseManager.dbMDPro3
import com.rarnu.mdpro3.database.entity.Deck
import com.rarnu.mdpro3.database.table.users
import com.rarnu.mdpro3.define.*
import com.rarnu.mdpro3.response.ResultWithValue
import com.rarnu.mdpro3.util.MCTokenValidation
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.ktorm.dsl.eq
import org.ktorm.entity.find

/**
 * 验证卡组信息是否正确
 */
suspend fun ApplicationCall.validateDeck(deck: Deck, needId: Boolean = false): Boolean? {
    if (needId) {
        if (deck.deckId.isBlank()) {
            errorRespond(ERR_NO_DECK_ID)
            return null
        }
    }
    if (deck.deckContributor.isBlank()) {
        errorRespond(ERR_NO_CONTRIBUTOR)
        return null
    }
    if (deck.deckName.isBlank()) {
        errorRespond(ERR_NO_DECK_NAME)
        return null
    }
    // 敏感词判断，这里把卡组名称和贡献者名称拼在一起查，主要是为了减少查库次数
    val sw = validateWord("${deck.deckName},${deck.deckContributor}")
    if (sw.isNotEmpty()) {
        respond(ResultWithValue.errorNoData(code = ERR_NAME_IS_SENSITIVE.first, message = ERR_NAME_IS_SENSITIVE.second, messageValue = sw.joinToString(",")))
        return null
    }
    if (deck.deckYdk.isNullOrBlank()) {
        errorRespond(ERR_NO_YDK)
        return null
    }

    if (deck.deckYdk?.replace("\r", "")?.split("\n")?.isValidYdk() != true) {
        errorRespond(ERR_YDK_NOT_VALID)
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
        errorRespond(ERR_NO_USER_ID)
        return null
    }
    return userId
}

suspend fun ApplicationCall.validateReqUserId(userId: Long): Boolean? {
    if (userId == 0L) {
        errorRespond(ERR_NO_USER_ID)
        return null
    }
    return true
}

suspend fun ApplicationCall.validateId(): String? {
    val id = parameters["id"]
    if (id.isNullOrBlank()) {
        errorRespond(ERR_NO_DECK_ID)
        return null
    }
    return id
}

suspend fun ApplicationCall.validatePuzzleId(): Long? {
    val id = parameters["id"]?.toLongOrNull()
    if (id == null) {
        errorRespond(ERR_NO_PUZZLE_ID)
        return null
    }
    return id

}

suspend fun ApplicationCall.validateSource(): Boolean? {
    val src = request.header("ReqSource")
    if (src.isNullOrBlank() || src != "MDPro3") {
        errorRespond(ERR_REQ_SOURCE)
        return null
    }
    return true
}

/**
 * 验证用户的 token
 */
suspend fun ApplicationCall.validateToken(userId: Long): Boolean? {
    val token = request.header("token")
    if (token.isNullOrBlank()) {
        errorRespond(ERR_NO_TOKEN)
        return null
    }
    val cacheKey = "mc_${userId}_$token"
    val ret = CacheManager.getMcValidated(cacheKey) {
        MCTokenValidation.validate(token, userId)
    }
    if (!ret) {
        errorRespond(ERR_USER_VALIDATE)
        return null
    }
    return true
}

/**
 * 验证用于残局的管理员身份
 *
 * 验证不通过返回为 null，验证通过返回角色所代表的整型值
 */
suspend fun ApplicationCall.validatePuzzleAdmin(): Int? {
    val userId = request.header("userId")?.toLongOrNull()
    val token = request.header("token")
    if (userId == null) {
        errorRespond(ERR_NO_USER_ID)
        return null
    }
    if (token.isNullOrBlank()) {
        errorRespond(ERR_NO_TOKEN)
        return null
    }
    val cacheKey = "mc_${userId}_$token"
    val ret = CacheManager.getMcValidated(cacheKey) {
        MCTokenValidation.validate(token, userId)
    }
    if (!ret) {
        errorRespond(ERR_USER_VALIDATE)
        return null
    }
    return dbMDPro3.users.find { it.userId eq userId }?.roleId ?: 0
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