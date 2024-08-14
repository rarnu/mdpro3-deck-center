@file:Suppress("DuplicatedCode")

package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity
import java.time.LocalDateTime

interface Deck : Entity<Deck> {

    companion object : Entity.Factory<Deck>()

    var deckId: String
    var deckContributor: String
    var deckName: String
    var deckRank: Int
    var deckLike: Int
    var deckUploadDate: LocalDateTime
    var deckUpdateDate: LocalDateTime
    var deckCoverCard1: Long
    var deckCoverCard2: Long
    var deckCoverCard3: Long
    var deckCase: Long
    var deckProtector: Long
    var deckMainSerial: String
    var deckYdk: String?
    var userId: Long
    var isPublic: Boolean
    var description: String
    var isDelete: Boolean
}

fun Deck.fromUpdate(): Deck {
    val d = Deck {}
    d.deckId = this.deckId
    d.deckContributor = this.deckContributor
    d.deckName = this.deckName
    d.deckCoverCard1 = this.deckCoverCard1
    d.deckCoverCard2 = this.deckCoverCard2
    d.deckCoverCard3 = this.deckCoverCard3
    d.deckCase = this.deckCase
    d.deckProtector = this.deckProtector
    d.deckYdk = this.deckYdk
    d.userId = this.userId
    d.isPublic = this.isPublic
    d.description = this.description
    d.isDelete = this.isDelete
    return d
}