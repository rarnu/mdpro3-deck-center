package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity
import java.time.LocalDateTime

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
    var publishDate: LocalDateTime
}

