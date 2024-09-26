package com.rarnu.mdpro3.api

import com.rarnu.mdpro3.cache.CacheManager
import com.rarnu.mdpro3.database.DatabaseManager.dbMDPro3
import com.rarnu.mdpro3.database.table.decks
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import com.isyscore.kotlin.ktor.Result
import com.rarnu.mdpro3.request.MultiSyncReq
import com.rarnu.mdpro3.request.SingleSyncReq
import com.rarnu.mdpro3.request.SyncDeckReq
import com.rarnu.mdpro3.request.toDeck
import com.rarnu.mdpro3.define.ERR_SYNC_FAIL
import com.rarnu.mdpro3.ext.*
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
        // call.record("/sync/[get]")
        val cacheKey = "sync_get_user_$userId"
        val ret = CacheManager.get(cacheKey, isPublic = false) {
            dbMDPro3.decks.filter { (it.userId eq userId) and (it.isDelete eq false) }.sortedBy { it.deckUpdateDate.desc() }.sortedBy { it.deckUploadDate.desc() }.map { it }
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
        // call.record("/sync/multi")
        if (req.decks.isEmpty()) {
            // 没有要同步的卡组，直接返回正确
            call.respond(Result.successNoData())
            return@post
        }
        // 开启一个事务来批量同步卡组
        val ret = dbMDPro3.useTransaction { trans ->
            val list = req.decks.map {
                // 清掉缓存，防止影响查询
                val cacheKey2 = "deck_get_id_${it.deckId}"
                CacheManager.clean(cacheKey2)
                syncDeck(it, req.userId, req.deckContributor)
            }
            try {
                trans.commit()
                req.decks.size == list.count { it }
            } catch (e: Exception) {
                trans.rollback()
                false
            }
        }
        val cacheKey = "sync_get_user_${req.userId}"
        CacheManager.clean(cacheKey, isPublic = false)
        call.respond(if (ret) Result.success(data = req.decks.size) else Result.errorNoData(code = ERR_SYNC_FAIL.first, message = ERR_SYNC_FAIL.second))
    }

    /**
     * 同步单个卡组
     */
    post<SingleSyncReq>("/single") { req ->
        call.validateSource() ?: return@post
        call.validateReqUserId(req.userId) ?: return@post
        call.validateToken(req.userId) ?: return@post
        // call.record("/sync/single")
        val ret = syncDeck(req.deck, req.userId, req.deckContributor)
        // 清掉缓存，防止影响查询
        val cacheKey = "sync_get_user_${req.userId}"
        CacheManager.clean(cacheKey, isPublic = false)
        val cacheKey2 = "deck_get_id_${req.deck.deckId}"
        CacheManager.clean(cacheKey2)
        call.respond(Result.success(data = ret))
    }
}

private fun syncDeck(dr: SyncDeckReq, userId: Long, contributor: String): Boolean =
    if (dr.isDelete) {
        // 如果是删除，打个删除标签，不做物理删除
        val deck = dbMDPro3.decks.find { (it.deckId eq dr.deckId) and (it.userId eq userId) }
        if (deck != null) {
            deck.isDelete = true
            dbMDPro3.decks.update(deck) > 0
        } else false
    } else {
        var deck = dbMDPro3.decks.find { (it.deckId eq dr.deckId) and (it.userId eq userId) }
        if (deck == null) {
            // 新增
            deck = dr.toDeck(userId, contributor)
            dbMDPro3.decks.add(deck) > 0
        } else {
            if (!deck.isDelete) {
                // 如果卡组没有被删除，则更新它
                deck = dr.toDeck(userId, contributor, isUpdate = true)
                dbMDPro3.decks.update(deck) > 0
            } else false
        }
    }
