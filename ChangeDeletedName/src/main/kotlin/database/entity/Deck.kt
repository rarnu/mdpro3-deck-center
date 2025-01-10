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
