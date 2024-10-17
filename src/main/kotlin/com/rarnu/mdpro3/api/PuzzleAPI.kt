@file:Suppress("DuplicatedCode")

package com.rarnu.mdpro3.api

import com.isyscore.kotlin.common.createEntitySeq
import com.isyscore.kotlin.common.plus
import com.isyscore.kotlin.ktor.Result
import com.isyscore.kotlin.ktor.PagedData
import com.isyscore.kotlin.ktor.PagedResult
import com.isyscore.kotlin.ktor.errorRespond
import com.rarnu.mdpro3.cache.CacheManager
import com.rarnu.mdpro3.database.DatabaseManager.dbMDPro3
import com.rarnu.mdpro3.database.entity.Puzzle
import com.rarnu.mdpro3.database.entity.PuzzlePass
import com.rarnu.mdpro3.database.table.PuzzlePasses
import com.rarnu.mdpro3.database.table.Puzzles
import com.rarnu.mdpro3.database.table.puzzlePasses
import com.rarnu.mdpro3.database.table.puzzles
import com.rarnu.mdpro3.define.ERR_NOT_ADMIN
import com.rarnu.mdpro3.define.ERR_PUZZLE_NOT_EXISTS
import com.rarnu.mdpro3.define.ERR_PUZZLE_OWNER
import com.rarnu.mdpro3.ext.validatePuzzleAdmin
import com.rarnu.mdpro3.ext.validatePuzzleId
import com.rarnu.mdpro3.ext.validateSource
import com.rarnu.mdpro3.request.PuzzleAuditReq
import com.rarnu.mdpro3.request.PuzzlePassReq
import com.rarnu.mdpro3.response.PuzzleVO
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.removeIf
import org.ktorm.entity.update
import java.time.LocalDateTime

fun Route.puzzleAPI() = route("/puzzle") {

    /**
     * 获取残局列表，支持按名字搜索，分页
     */
    get("/list") {
        call.validateSource() ?: return@get
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20
        val keyWord = call.request.queryParameters["keyWord"]
        val contributor = call.request.queryParameters["contributor"]

        val cacheKey = "puzzle_get_list_page_${page}_size_${size}_key_${keyWord}_con_${contributor}"

        val ret = CacheManager.get(cacheKey) {
            val q = dbMDPro3.from(Puzzles)
                .leftJoin(PuzzlePasses, on = Puzzles.id eq PuzzlePasses.puzzleId)
                .select(
                    Puzzles.id, Puzzles.name, Puzzles.userId, Puzzles.contributor, Puzzles.message, Puzzles.solution, Puzzles.coverCard,
                    Puzzles.publishDate, Puzzles.luaScript, count(PuzzlePasses.userId)
                )
                .where {
                    var cond = Puzzles.audited eq 1
                    if (!keyWord.isNullOrBlank()) cond += Puzzles.name like "${keyWord}%"
                    if (!contributor.isNullOrBlank()) cond += Puzzles.contributor like "${contributor}%"
                    cond
                }
                .groupBy(Puzzles.id)
                .limit((page - 1) * size, size)
            val total = q.totalRecordsInAllPages
            val pages = (total / size) + (if (total % size == 0) 0 else 1)
            val list = q.map { createEntitySeq<PuzzleVO>(it) }
            PagedData(current = page, size = size, total = total, pages = pages, records = list)
        }

        call.respond(PagedResult.success(data = ret))
    }

    /**
     * 管理员获取要审核的残局
     */
    get("/tobeAudit") {
        call.validateSource() ?: return@get
        val role = call.validatePuzzleAdmin() ?: return@get
        if (role < 100) {
            call.errorRespond(ERR_NOT_ADMIN)
            return@get
        }
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20
        val contributor = call.request.queryParameters["contributor"]

        val q = dbMDPro3.from(Puzzles)
            .select(Puzzles.columns)
            .where {
                var cond = Puzzles.audited eq 0
                if (!contributor.isNullOrBlank()) cond += Puzzles.contributor like "${contributor}%"
                cond
            }
            .groupBy(Puzzles.id)
            .limit((page - 1) * size, size)
        val total = q.totalRecordsInAllPages
        val pages = (total / size) + (if (total % size == 0) 0 else 1)
        val list = q.map { Puzzles.createEntity(it) }
        val ret = PagedData(current = page, size = size, total = total, pages = pages, records = list)

        call.respond(PagedResult.success(data = ret))
    }

    /**
     * 管理员获取已被拒绝的残局
     */
    get("/rejected") {
        call.validateSource() ?: return@get
        val role = call.validatePuzzleAdmin() ?: return@get
        if (role < 100) {
            call.errorRespond(ERR_NOT_ADMIN)
            return@get
        }
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20
        val contributor = call.request.queryParameters["contributor"]


        val q = dbMDPro3.from(Puzzles)
            .select(Puzzles.columns)
            .where {
                var cond = Puzzles.audited eq 2
                if (!contributor.isNullOrBlank()) cond += Puzzles.contributor like "${contributor}%"
                cond
            }
            .groupBy(Puzzles.id)
            .limit((page - 1) * size, size)
        val total = q.totalRecordsInAllPages
        val pages = (total / size) + (if (total % size == 0) 0 else 1)
        val list = q.map { Puzzles.createEntity(it) }
        val ret = PagedData(current = page, size = size, total = total, pages = pages, records = list)

        call.respond(PagedResult.success(data = ret))
    }

    /**
     * 变更通关状态,仅登录用户可用
     */
    post<PuzzlePassReq>("/pass") {
        call.validateSource() ?: return@post
        call.validatePuzzleAdmin() ?: return@post
        val ret = try {
            dbMDPro3.puzzlePasses.add(PuzzlePass {
                this.puzzleId = it.puzzleId
                this.userId = it.userId
                this.passTime = LocalDateTime.now()
            }) > 0
        } catch (e: Exception) {
            false
        }
        call.respond(Result.successNoData(message = "$ret"))
    }

    /**
     * 删除一个残局，仅管理员可用
     */
    delete("/delete/{id}") {
        call.validateSource() ?: return@delete
        val role = call.validatePuzzleAdmin() ?: return@delete
        if (role < 100) {
            call.errorRespond(ERR_NOT_ADMIN)
            return@delete
        }
        val id = call.validatePuzzleId() ?: return@delete
        val ret = try {
            // 删残局
            val succ = dbMDPro3.puzzles.removeIf { it.id eq id } > 0
            if (succ) {
                // 删通关记录
                dbMDPro3.puzzlePasses.removeIf { it.puzzleId eq id }
            }
            succ
        } catch (e: Exception) {
            false
        }
        call.respond(Result.successNoData(message = "$ret"))
    }

    /**
     * 变更残局的审核状态(仅管理员)
     */
    post<PuzzleAuditReq>("/audit") { req ->
        call.validateSource() ?: return@post
        val role = call.validatePuzzleAdmin() ?: return@post
        if (role < 100) {
            call.errorRespond(ERR_NOT_ADMIN)
            return@post
        }
        val ret = try {
            dbMDPro3.update(Puzzles) {
                set(Puzzles.audited, req.audit)
                where { Puzzles.id eq req.puzzleId }
            } > 0
        } catch (e: Exception) {
            false
        }
        call.respond(Result.successNoData(message = "$ret"))
    }

    /**
     * 上传一个残局
     */
    post<Puzzle> {
        call.validateSource() ?: return@post
        call.validatePuzzleAdmin() ?: return@post
        val ret = try {
            it.audited = 0
            dbMDPro3.puzzles.add(it) > 0
        } catch (e: Exception) {
            false
        }
        call.respond(Result.successNoData(message = "$ret"))
    }

    /**
     * 更新一个残局
     */
    put<Puzzle> {req ->
        call.validateSource() ?: return@put
        call.validatePuzzleAdmin() ?: return@put
        val p = dbMDPro3.puzzles.find { it.id eq req.id }
        if (p == null) {
            call.errorRespond(ERR_PUZZLE_NOT_EXISTS)
            return@put
        }
        if (p.userId != req.userId) {
            call.errorRespond(ERR_PUZZLE_OWNER)
            return@put
        }
        val ret = try {
            req.audited = 0
            req.publishDate = LocalDateTime.now()
            dbMDPro3.puzzles.update(req) > 0
        } catch (e: Exception) {
            false
        }
        call.respond(Result.successNoData(message = "$ret"))
    }

    /**
     * 新增或更新残局
     *
     * id 为 0 表示新增，不为 0 表示更新
     */
    post<Puzzle>("/save") {
        // TODO: 新增或更新残局
    }
}