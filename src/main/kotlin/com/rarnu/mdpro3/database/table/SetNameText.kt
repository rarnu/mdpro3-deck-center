package com.rarnu.mdpro3.database.table

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

interface SetNameText: Entity<SetNameText> {

    companion object: Entity.Factory<SetNameText>()

    var id: Long
    var en: String
    var kanji: String
    var kk: String
    var donetime: Long

}

object SetNameTexts : Table<SetNameText>("set_name_texts") {

    var id = long("id").primaryKey().bindTo { it.id }
    var en = varchar("en").primaryKey().bindTo { it.en }
    var kanji = varchar("kanji").bindTo { it.kanji }
    var kk = varchar("kk").bindTo { it.kk }
    var donetime = long("donetime").bindTo { it.donetime }

}

val Database.setNameTexts get() = this.sequenceOf(SetNameTexts)