package com.rarnu.mdpro3.api

import com.isyscore.kotlin.ktor.Result
import com.rarnu.mdpro3.cache.CacheManager
import com.rarnu.mdpro3.database.DatabaseManager.db
import com.rarnu.mdpro3.database.entity.vo.WordReq
import com.rarnu.mdpro3.define.ERR_NO_WORD
import com.rarnu.mdpro3.ext.record
import com.rarnu.mdpro3.ext.validateSource
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.sensitiveWordAPI() = route("/sw") {

    /**
     * 敏感词判定
     */
    post<WordReq>("/validate") {
        call.validateSource() ?: return@post
        call.record("/sw/validate")
        if (it.text.isBlank()) {
            call.respond(Result.errorNoData(code = ERR_NO_WORD.first, message = ERR_NO_WORD.second))
            return@post
        }
        val cacheKey = "sw_validate_text_${it.text}"
        val ret = CacheManager.get(cacheKey) {
            validateWord(it.text)
        }
        call.respond(Result.success(message = "${ret.isEmpty()}", data = ret))
    }
}

/**
 * 输入文本，返回命中的关键字列表
 */
fun validateWord(word: String): List<String> = db.useConnection { conn ->
    val list = mutableListOf<String>()
    conn.prepareStatement("select word from sensitive_word where ? like concat('%', word, '%')").use { stmt ->
        stmt.setString(1, word)
        stmt.executeQuery().use { rs ->
            while (rs.next()) {
                list.add(rs.getString(1))
            }
        }
    }
    list
}

