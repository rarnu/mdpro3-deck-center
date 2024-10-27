@file:Suppress("DuplicatedCode")

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
