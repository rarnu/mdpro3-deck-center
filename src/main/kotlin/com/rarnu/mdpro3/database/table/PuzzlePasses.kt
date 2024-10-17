package com.rarnu.mdpro3.database.table

import com.rarnu.mdpro3.database.entity.PuzzlePass
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.long

object PuzzlePasses: Table<PuzzlePass>("puzzle_pass") {

    var puzzleId = long("puzzle_id").primaryKey().bindTo { it.puzzleId }
    var userId = long("user_id").primaryKey().bindTo { it.userId }
    var passTime = datetime("pass_time").bindTo { it.passTime }
}

val Database.puzzlePasses get() = this.sequenceOf(PuzzlePasses)