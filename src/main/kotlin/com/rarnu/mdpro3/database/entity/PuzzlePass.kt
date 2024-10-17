package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity
import java.time.LocalDateTime


interface PuzzlePass: Entity<PuzzlePass> {

    companion object: Entity.Factory<PuzzlePass>()

    var puzzleId: Long
    var userId: Long
    var passTime: LocalDateTime

}