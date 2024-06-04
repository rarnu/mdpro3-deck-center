package com.rarnu.mdpro3.ext

import com.isyscore.kotlin.ktor.Result
import com.rarnu.mdpro3.database.entity.Deck
import com.rarnu.mdpro3.define.*
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
    if (deck.deckYdk.isNullOrBlank()) {
        respond(Result.errorNoData(code = ERR_NO_YDK.first, message = ERR_NO_YDK.second))
        return null
    }

    if (deck.deckYdk?.split("\n")?.isValidYdk() != true) {
        respond(Result.errorNoData(code = ERR_YDK_NOT_VALID.first, message = ERR_YDK_NOT_VALID.second))
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
        respond(Result.errorNoData(code = ERR_REQ_SOURCE.first, message = ERR_REQ_SOURCE.second))
        return null
    }
    return true
}

private fun List<String>.isValidYdk(): Boolean {
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