package com.rarnu.mdpro3.database.table

import com.rarnu.mdpro3.database.entity.Deck
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*

object Decks : Table<Deck>("deck") {

    var deckId = varchar("deck_id").primaryKey().bindTo { it.deckId }
    var deckContributor = varchar("deck_contributor").bindTo { it.deckContributor }
    var deckName = varchar("deck_name").bindTo { it.deckName }
    var deckRank = int("deck_rank").bindTo { it.deckRank }
    var deckLike = int("deck_like").bindTo { it.deckLike }
    var deckUploadDate = datetime("deck_upload_date").bindTo { it.deckUploadDate }
    var deckUpdateDate = datetime("deck_update_date").bindTo { it.deckUpdateDate }
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