package com.rarnu.mdpro3.database.table

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.timestamp
import java.time.Instant


interface PuzzlePass : Entity<PuzzlePass> {

    companion object : Entity.Factory<PuzzlePass>()

    var puzzleId: Long
    var userId: Long
    var passTime: Instant

}

object PuzzlePasses : Table<PuzzlePass>("puzzle_pass") {

    var puzzleId = long("puzzle_id").primaryKey().bindTo { it.puzzleId }
    var userId = long("user_id").primaryKey().bindTo { it.userId }
    var passTime = timestamp("pass_time").bindTo { it.passTime }
}

val Database.puzzlePasses get() = this.sequenceOf(PuzzlePasses)