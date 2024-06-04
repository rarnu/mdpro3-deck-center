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
    var deckMainSerial = varchar("deck_main_serial").bindTo { it.deckMainSerial }
    var deckYdk = text("deck_ydk").bindTo { it.deckYdk }
}

val Database.decks get() = this.sequenceOf(Decks)