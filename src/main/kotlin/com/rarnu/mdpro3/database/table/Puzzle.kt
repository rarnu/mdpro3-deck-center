package com.rarnu.mdpro3.database.table

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.Instant


interface Puzzle: Entity<Puzzle> {

    companion object: Entity.Factory<Puzzle>()

    var id: Long
    var name: String
    var userId: Long
    var contributor: String
    var message: String
    var solution: String
    var coverCard: Long
    var luaScript: String
    var audited: Int
    var publishDate: Instant
}

fun Puzzle.copyForAdd(): Puzzle {
    val p = Puzzle { }
    p.name = this.name
    p.userId = this.userId
    p.contributor = this.contributor
    p.message = this.message
    p.solution = this.solution
    p.coverCard = this.coverCard
    p.luaScript = this.luaScript
    return p
}


object Puzzles : Table<Puzzle>("puzzle") {

    var id = long("id").primaryKey().bindTo { it.id }
    var name = varchar("name").bindTo { it.name }
    var userId = long("user_id").bindTo { it.userId }
    var contributor = varchar("contributor").bindTo { it.contributor }
    var message = text("message").bindTo { it.message }
    var solution = text("solution").bindTo { it.solution }
    var coverCard = long("cover_card").bindTo { it.coverCard }
    var luaScript = text("lua_script").bindTo { it.luaScript }
    var audited = int("audited").bindTo { it.audited }
    var publishDate = timestamp("publish_date").bindTo { it.publishDate }
}

val Database.puzzles get() = this.sequenceOf(Puzzles)