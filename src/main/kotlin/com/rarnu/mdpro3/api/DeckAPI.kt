@file:Suppress("DuplicatedCode")

package com.rarnu.mdpro3.api

import com.isyscore.kotlin.ktor.PagedData
import com.isyscore.kotlin.ktor.PagedResult
import com.isyscore.kotlin.ktor.Result
import com.rarnu.mdpro3.cache.CacheManager
import com.rarnu.mdpro3.database.DatabaseManager.db
import com.rarnu.mdpro3.database.entity.Deck
import com.rarnu.mdpro3.database.entity.fromUpdate
import com.rarnu.mdpro3.database.entity.vo.*
import com.rarnu.mdpro3.database.table.Decks
import com.rarnu.mdpro3.database.table.decks
import com.rarnu.mdpro3.define.*
import com.rarnu.mdpro3.ext.*
import com.rarnu.mdpro3.util.CardSerial
import com.rarnu.mdpro3.util.IdGenerator
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import org.ktorm.entity.*
import java.time.LocalDateTime

fun Route.deckAPI() = route("/deck") {

    /**
     * 让服务器生成一个唯一的卡组Id
     */
    get("/deckId") {
        call.validateSource() ?: return@get
        call.record("/deck/deckId")
        call.respond(Result.success(data = IdGenerator.nextIdDB()))
    }

    /**
     * 让服务器生成指定数量的卡组Id
     */
    get("/deckIds") {
        call.validateSource() ?: return@get
        call.record("/deck/deckIds")
        val count = call.request.queryParameters["count"]?.toIntOrNull() ?: 0
        val ret = (0 until count).map { IdGenerator.nextIdDB() }
        call.respond(Result.success(data = ret))
    }

    /**
     * 更新卡组公开状态
     */
    post<DeckPublicReq>("/public") { req ->
        call.validateSource() ?: return@post
        call.validateReqUserId(req.userId) ?: return@post
        call.validateToken(req.userId) ?: return@post
        call.record("/deck/public")
        val ret = db.update(Decks) {
            set(Decks.isPublic, req.isPublic)
            where { (Decks.deckId eq req.deckId) and (Decks.userId eq req.userId) }
        } > 0
        call.respond(Result.successNoData(message = "$ret"))
    }

    /**
     * 更新卡组描述
     */
    post<DeckDescriptionReq>("/description") { req ->
        call.validateSource() ?: return@post
        call.validateReqUserId(req.userId) ?: return@post
        call.validateToken(req.userId) ?: return@post
        call.record("/deck/description")
        val ret = db.update(Decks) {
            set(Decks.description, req.description)
            where { (Decks.deckId eq req.deckId) and (Decks.userId eq req.userId) }
        } > 0
        call.respond(Result.successNoData(message = "$ret"))
    }

    /**
     * @Deprecated 已过期
     * 上传卡组，返回卡组 id
     */
    post<Deck>("/upload") {
        call.validateSource() ?: return@post
        call.validateDeck(it, false) ?: return@post
        call.record("/deck/upload")
        it.deckId = IdGenerator.nextIdDB()
        it.deckUploadDate = LocalDateTime.now()
        it.deckUpdateDate = LocalDateTime.now()
        it.deckMainSerial = CardSerial.getCardSerial(listOf(it.deckCoverCard1, it.deckCoverCard2, it.deckCoverCard3))
        it.isPublic = true
        val (succ, err) = try {
            (db.decks.add(it) > 0) to ""
        } catch (e: Exception) {
            false to e.message
        }
        if (succ) {
            call.respond(Result.success(data = it))
        } else {
            call.respond(Result.error(code = ERR_UPLOAD_DECK.first, message = ERR_UPLOAD_DECK.second, data = "$err"))
        }
    }

    /**
     * @Deprecated 已过期
     * 更新卡组
     */
    put<Deck>("/update") {
        call.validateSource() ?: return@put
        call.validateDeck(it, true) ?: return@put
        call.record("/deck/update")
        val d = it.fromUpdate()
        d.deckUpdateDate = LocalDateTime.now()
        d.deckMainSerial = CardSerial.getCardSerial(listOf(d.deckCoverCard1, d.deckCoverCard2, d.deckCoverCard3))
        d.isPublic = true
        val (succ, err) = try {
            (db.decks.update(d) > 0) to ""
        } catch (e: Exception) {
            false to e.message
        }
        if (succ) {
            call.respond(Result.success(data = d))
        } else {
            call.respond(Result.error(code = ERR_UPDATE_DECK.first, message = ERR_UPDATE_DECK.second, data = "$err"))
        }
    }

    /**
     * @Deprecated 已过期
     * 删除卡组(管理员功能)
     */
    delete("/{id}") {
        call.validateSource() ?: return@delete
        val id = call.validateId() ?: return@delete
        call.record("/deck/[delete]")
        val (succ, err) = try {
            (db.decks.removeIf { it.deckId eq id } > 0) to ""
        } catch (e: Exception) {
            false to e.message
        }
        if (succ) {
            call.respond(Result.successNoData())
        } else {
            call.respond(Result.error(code = ERR_DELETE_DECK.first, message = ERR_DELETE_DECK.second, data = "$err"))
        }
    }

    /**
     * 根据卡组 ID 查询一个卡组的内容
     */
    get("/{id}") {
        call.validateSource() ?: return@get
        val id = call.validateId() ?: return@get
        call.record("/deck/[get]")
        val cacheKey = "deck_get_id_${id}"
        val ret = CacheManager.getOrNull(cacheKey) {
            db.decks.find { it.deckId eq id }
        }
        if (ret != null) {
            call.respond(Result.success(data = ret))
        } else {
            call.respond(Result.errorNoData(code = ERR_DECK_NOT_EXISTS.first, message = ERR_DECK_NOT_EXISTS.second))
        }
    }

    /**
     * 分页查询卡组，带有排序方式，默认按更新时间/上传时间倒序
     */
    get("/list") {
        call.validateSource() ?: return@get
        call.record("/deck/list")
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20
        val keyWord = call.request.queryParameters["keyWord"]
        val sortLike = call.request.queryParameters["sortLike"]?.toBoolean() ?: false
        val sortRank = call.request.queryParameters["sortRank"]?.toBoolean() ?: false
        val contributor = call.request.queryParameters["contributor"]

        val cacheKey = "deck_get_list_page_${page}_size_${size}_key_${keyWord}_con_${contributor}_like_${sortLike}_rank_${sortRank}"

        db.from(Decks).select().whereWithConditions { list ->
            list += (Decks.deckId.isNotNull())
        }

        val ret = CacheManager.get(cacheKey) {
            var q = db.from(Decks).select(Decks.columns).where {
                var dec = Decks.isPublic eq true
                if (!keyWord.isNullOrBlank()) dec = dec and ((Decks.deckName like "%$keyWord%") or (Decks.deckMainSerial like "%$keyWord%") or (Decks.deckId like "%$keyWord%"))
                if (!contributor.isNullOrBlank()) dec = dec and (Decks.deckContributor like "%$contributor%")
                dec
            }
            q = when {
                sortLike -> q.orderBy(Decks.deckLike.desc())
                sortRank -> q.orderBy(Decks.deckRank.desc())
                else -> q.orderBy(Decks.deckUpdateDate.desc(), Decks.deckUploadDate.desc())
            }
            q = q.limit((page - 1) * size, size)
            val total = q.totalRecordsInAllPages
            val pages = (total / size) + (if (total % size == 0) 0 else 1)
            val list = q.map { DeckLiteVO.fromRow(it) /*Decks.createEntity(it)*/ }
            PagedData(current = page, size = size, total = total, pages = pages, records = list)
        }

        call.respond(PagedResult.success(data = ret))
    }

    /**
     * 给卡组点赞
     */
    post("/like/{id}") {
        call.validateSource() ?: return@post
        val id = call.validateId() ?: return@post
        call.record("/deck/like")
        val ip = call.request.origin.remoteHost
        val cacheKey = "remote_host_${ip}_deck_${id}"
        if (CacheManager.hasLike(cacheKey)) {
            call.respond(Result.errorNoData(code = ERR_LIKE_TOO_NEAR.first, message = ERR_LIKE_TOO_NEAR.second))
            return@post
        }
        val succ = db.update(Decks) {
            set(Decks.deckLike, Decks.deckLike plus 1)
            where { Decks.deckId eq id }
        } > 0
        CacheManager.accessLike(cacheKey)
        call.respond(Result.successNoData(message = "$succ"))
    }

    /**
     * 设置 rank（管理员功能）
     */
    post<RankReq>("/rank/{id}") { req ->
        call.validateSource() ?: return@post
        val id = call.validateId() ?: return@post
        call.record("/deck/rank")
        val succ = db.update(Decks) {
            set(Decks.deckRank, req.rank)
            where { Decks.deckId eq id }
        } > 0
        call.respond(Result.successNoData(message = "$succ"))
    }

    /**
     * 返回最简的数据结结构
     */
    get("/list/lite") {
        call.validateSource() ?: return@get
        call.record("/deck/list/lite")
        val size = kotlin.math.min(call.request.queryParameters["size"]?.toIntOrNull() ?: 1000, 1000)
        val keyWord = call.request.queryParameters["keyWord"]
        val sortLike = call.request.queryParameters["sortLike"]?.toBoolean() ?: false
        val sortRank = call.request.queryParameters["sortRank"]?.toBoolean() ?: false
        val contributor = call.request.queryParameters["contributor"]

        val cacheKey = "deck_get_list_lite_size_${size}_key_${keyWord}_con_${contributor}_like_${sortLike}_rank_${sortRank}"
        val ret = CacheManager.get(cacheKey) {
            var q = db.from(Decks).select(Decks.columns).where {
                var dec = (Decks.isPublic eq true) and (Decks.isDelete eq false)
                if (!keyWord.isNullOrBlank()) dec = dec and ((Decks.deckName like "%$keyWord%") or (Decks.deckMainSerial like "%$keyWord%") or (Decks.deckId like "%$keyWord%"))
                if (!contributor.isNullOrBlank()) dec = dec and (Decks.deckContributor like "%$contributor%")
                dec
            }
            q = when {
                sortLike -> q.orderBy(Decks.deckLike.desc())
                sortRank -> q.orderBy(Decks.deckRank.desc())
                else -> q.orderBy(Decks.deckUpdateDate.desc(), Decks.deckUploadDate.desc())
            }
            q = q.limit(size)
            q.map { DeckLiteVO.fromRow(it) }
        }
        call.respond(Result.success(data = ret))
    }

}