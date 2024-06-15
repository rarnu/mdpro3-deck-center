package com.rarnu.mdpro3.database.table

import com.rarnu.mdpro3.database.entity.DeckId
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.varchar

object DeckIds : Table<DeckId>("deck_id") {
    var id = varchar("id").primaryKey().bindTo { it.id }
}

val Database.deckIds get() = this.sequenceOf(DeckIds)