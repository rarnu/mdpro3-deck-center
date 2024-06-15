package com.rarnu.mdpro3.api

import com.rarnu.mdpro3.cache.CacheManager
import com.rarnu.mdpro3.database.DatabaseManager.db
import com.rarnu.mdpro3.database.table.decks
import com.rarnu.mdpro3.ext.validateToken
import com.rarnu.mdpro3.ext.validateUserId
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import com.isyscore.kotlin.ktor.Result
import com.rarnu.mdpro3.database.entity.vo.MultiSyncReq
import com.rarnu.mdpro3.database.entity.vo.SingleSyncReq
import com.rarnu.mdpro3.database.entity.vo.SyncDeckReq
import com.rarnu.mdpro3.database.entity.vo.toDeck
import com.rarnu.mdpro3.define.ERR_SYNC_FAIL
import com.rarnu.mdpro3.ext.validateReqUserId
import com.rarnu.mdpro3.ext.validateSource
import org.ktorm.dsl.and
import org.ktorm.entity.*

fun Route.syncAPI() = route("/sync") {

    /**
     * 获取指定用户的卡组列表
     *
     * 这里的卡组列表是带有 ydk 内容的
     */
    get("/{userId}") {
        call.validateSource() ?: return@get
        val userId = call.validateUserId() ?: return@get
        call.validateToken(userId) ?: return@get
        val cacheKey = "sync_get_user_$userId"
        val ret = CacheManager.get(cacheKey) {
            db.decks.filter { it.userId eq userId }.sortedBy { it.deckUpdateDate.desc() }.sortedBy { it.deckUploadDate.desc() }.map { it }
        }
        call.respond(Result.success(data = ret))
    }

    /**
     * 同步用户的所有卡组
     */
    post<MultiSyncReq>("/multi") { req ->
        call.validateSource() ?: return@post
        call.validateReqUserId(req.userId) ?: return@post
        call.validateToken(req.userId) ?: return@post
        if (req.decks.isEmpty()) {
            // 没有要同步的卡组，直接返回正确
            call.respond(Result.successNoData())
            return@post
        }
        // 开启一个事务来批量同步卡组
        val ret = db.useTransaction { trans ->
            val list = req.decks.map { syncDeck(it, req.userId, req.deckContributor) }
            try {
                trans.commit()
                req.decks.size == list.count { it }
            } catch (e: Exception) {
                trans.rollback()
                false
            }
        }
        call.respond(if (ret) Result.success(data = req.decks.size) else Result.errorNoData(code = ERR_SYNC_FAIL.first, message = ERR_SYNC_FAIL.second))
    }

    /**
     * 同步单个卡组
     */
    post<SingleSyncReq>("/single") { req ->
        call.validateSource() ?: return@post
        call.validateReqUserId(req.userId) ?: return@post
        call.validateToken(req.userId) ?: return@post
        val ret = syncDeck(req.deck, req.userId, req.deckContributor)
        call.respond(Result.success(data = ret))
    }
}

private fun syncDeck(dr: SyncDeckReq, userId: Long, contributor: String): Boolean =
    if (dr.isDelete) {
        // 删除
        db.decks.removeIf { (it.deckId eq dr.deckId) and (it.userId eq userId) } > 0
    } else {
        var deck = db.decks.find { (it.deckId eq dr.deckId) and (it.userId eq userId) }
        if (deck == null) {
            // 新增
            deck = dr.toDeck(userId, contributor)
            db.decks.add(deck) > 0
        } else {
            // 更新
            deck = dr.toDeck(userId, contributor, isUpdate = true)
            db.decks.update(deck) > 0
        }
    }
