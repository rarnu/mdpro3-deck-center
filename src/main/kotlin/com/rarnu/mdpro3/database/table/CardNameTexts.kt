package com.rarnu.mdpro3.database.table

import com.rarnu.mdpro3.database.entity.CardNameText
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object CardNameTexts : Table<CardNameText>("card_name_texts") {

    var id = long("id").primaryKey().bindTo { it.id }
    var kanji = varchar("kanji").bindTo { it.kanji }
    var kk = varchar("kk").bindTo { it.kk }
    var donetime = long("donetime").bindTo { it.donetime }
}

val Database.cardNameTexts get() = this.sequenceOf(CardNameTexts)