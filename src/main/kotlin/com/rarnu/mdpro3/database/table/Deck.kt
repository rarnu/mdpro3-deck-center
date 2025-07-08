package com.rarnu.mdpro3.database.table

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.Instant


interface Deck : Entity<Deck> {

    companion object : Entity.Factory<Deck>()

    var deckId: String
    var deckContributor: String
    var deckName: String
    var deckRank: Int
    var deckLike: Int
    var deckUploadDate: Instant
    var deckUpdateDate: Instant
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

object Decks : Table<Deck>("deck") {

    var deckId = varchar("deck_id").primaryKey().bindTo { it.deckId }
    var deckContributor = varchar("deck_contributor").bindTo { it.deckContributor }
    var deckName = varchar("deck_name").bindTo { it.deckName }
    var deckRank = int("deck_rank").bindTo { it.deckRank }
    var deckLike = int("deck_like").bindTo { it.deckLike }
    var deckUploadDate = timestamp("deck_upload_date").bindTo { it.deckUploadDate }
    var deckUpdateDate = timestamp("deck_update_date").bindTo { it.deckUpdateDate }
    var deckCoverCard1 = long("deck_cover_card1").bindTo { it.deckCoverCard1 }
    var deckCoverCard2 = long("deck_cover_card2").bindTo { it.deckCoverCard2 }
    var deckCoverCard3 = long("deck_cover_card3").bindTo { it.deckCoverCard3 }
    var deckCase = long("deck_case").bindTo { it.deckCase }
    var deckProtector = long("deck_protector").bindTo { it.deckProtector }
    var deckMainSerial = varchar("deck_main_serial").bindTo { it.deckMainSerial }
    var deckYdk = text("deck_ydk").bindTo { it.deckYdk }
    var userId = long("user_id").bindTo { it.userId }
    var isPublic = int("is_public").transform({ it == 1 }, { if (it) 1 else 0 }).bindTo { it.isPublic }
    var description = text("description").bindTo { it.description }
    var isDelete = int("is_delete").transform({ it == 1 }, { if (it) 1 else 0 }).bindTo { it.isDelete }
}

val Database.decks get() = this.sequenceOf(Decks)