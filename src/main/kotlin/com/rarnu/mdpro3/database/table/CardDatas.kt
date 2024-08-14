package com.rarnu.mdpro3.database.table

import com.rarnu.mdpro3.database.entity.CardData
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.blob
import org.ktorm.schema.int
import org.ktorm.schema.long

object CardDatas : Table<CardData>("datas") {

    var id = long("id").primaryKey().bindTo { it.id }
    var ot = int("ot").bindTo { it.ot }
    var _alias = long("alias").bindTo { it.alias }
    var setcode = long("setcode").bindTo { it.setcode }
    var type = long("type").bindTo { it.type }
    var atk = int("atk").bindTo { it.atk }
    var def = int("def").bindTo { it.def }
    var level = int("level").bindTo { it.level }
    var race = long("race").bindTo { it.race }
    var attribute = long("attribute").bindTo { it.attribute }
    var category = long("category").bindTo { it.category }
    var genre = long("genre").bindTo { it.genre }
    var script = blob("script").bindTo { it.script }
    var support = long("support").bindTo { it.support }
    var ocgdate = long("ocgdate").bindTo { it.ocgdate }
    var tcgdate = long("tcgdate").bindTo { it.tcgdate }
}

val Database.cardDatas get() = this.sequenceOf(CardDatas)