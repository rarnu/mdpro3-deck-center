package com.rarnu.mdpro3.database.table

import com.rarnu.mdpro3.database.entity.Puzzle
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*

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
    var publishDate = datetime("publish_date").bindTo { it.publishDate }
}

val Database.puzzles get() = this.sequenceOf(Puzzles)