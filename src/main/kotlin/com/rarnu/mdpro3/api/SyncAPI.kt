package com.rarnu.mdpro3.api

import com.rarnu.mdpro3.cache.CacheManager
import com.rarnu.mdpro3.database.DatabaseManager.dbMDPro3
import com.rarnu.mdpro3.database.table.decks
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.eq
import com.isyscore.kotlin.ktor.Result
import com.rarnu.mdpro3.request.MultiSyncReq
import com.rarnu.mdpro3.request.SingleSyncReq
import com.rarnu.mdpro3.request.SyncDeckReq
import com.rarnu.mdpro3.request.toDeck
import com.rarnu.mdpro3.define.ERR_SYNC_FAIL
import com.rarnu.mdpro3.ext.*
import com.rarnu.mdpro3.util.SnowFlakeManager
import io.ktor.server.request.*
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
            // 只查 userId，因为被删除的卡组也要返回
            dbMDPro3.decks.filter { it.userId eq userId }.sortedByDescending { it.deckUpdateDate }.map {
                // 如果是已删的卡组，将 ydk 内容置空
                if (it.isDelete) it.deckYdk = ""
                it
            }
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
                call.syncDeck(it, req.userId, req.deckContributor)
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
        val ret = call.syncDeck(req.deck, req.userId, req.deckContributor)
        // 清掉缓存，防止影响查询
        val cacheKey = "sync_get_user_${req.userId}"
        CacheManager.clean(cacheKey, isPublic = false)
        val cacheKey2 = "deck_get_id_${req.deck.deckId}"
        CacheManager.clean(cacheKey2)
        call.respond(Result.success(data = ret))
    }
}

private fun ApplicationCall.syncDeck(dr: SyncDeckReq, userId: Long, contributor: String): Boolean = try {
    if (dr.isDelete) {
        // 如果是删除，打个删除标签，不做物理删除
        val deck = dbMDPro3.decks.find { (it.deckId eq dr.deckId) and (it.userId eq userId) }
        if (deck != null) {
            deck.isDelete = true
            //　将卡组名称改为 del　+ 随机码，以便允许用户再写入相同的卡组名
            deck.deckName = "del_" + SnowFlakeManager.nextSnowId() + "_eted"
            val succ = dbMDPro3.decks.update(deck) > 0
            // application.log.error("请求删除卡组 [${request.path()}] [user = $userId] [deck = ${dr.deckId}] 删除情况: $succ")
            succ
        } else {
            // application.log.error("请求删除卡组 [${request.path()}] [user = $userId] [deck = ${dr.deckId}] 卡组不存在")
            false
        }
    } else {
        // 如果不是删除，先找这个卡组
        var deck = dbMDPro3.decks.find { (it.deckId eq dr.deckId) and (it.userId eq userId) }
        if (deck == null) {
            // 如果没找到，新增
            deck = dr.toDeck(userId, contributor)
            val succ = dbMDPro3.decks.add(deck) > 0
            // application.log.error("请求新增卡组 [${request.path()}] [user = $userId] [deck = ${dr.deckId}] 新增情况: $succ")
            succ
        } else {
            // 如果找到了，并且不是已删除状态，则更新
            if (!deck.isDelete) {
                deck = dr.toDeck(userId, contributor, isUpdate = true)
                val succ = dbMDPro3.decks.update(deck) > 0
                // application.log.error("请求更新卡组 [${request.path()}] [user = $userId] [deck = ${dr.deckId}] 更新情况: $succ")
                succ
            } else {
                // application.log.error("请求更新卡组 [${request.path()}] [user = $userId] [deck = ${dr.deckId}] 卡组不存在")
                false
            }
        }
    }
} catch (e: Exception) {
    // application.log.error("请求同步卡组 [${request.path()}] [user = $userId] [deck = ${dr.deckId}] 失败，原因是: $e")
    false
}
