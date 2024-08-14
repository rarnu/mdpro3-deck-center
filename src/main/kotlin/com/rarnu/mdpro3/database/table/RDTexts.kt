package com.rarnu.mdpro3.database.table

import com.rarnu.mdpro3.database.entity.RDText
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object RDTexts: Table<RDText>("") {

    var id = long("id").primaryKey().bindTo { it.id }
    var name = varchar("name").bindTo { it.name }
    var kk = varchar("kk").bindTo { it.kk }
    var desc = varchar("desc").bindTo { it.desc }
    var type = long("type").bindTo { it.type }
    var atk = int("atk").bindTo { it.atk }
    var def = int("def").bindTo { it.def }
    var level = int("level").bindTo { it.level }
    var race = long("race").bindTo { it.race }
    var attribute = long("attribute").bindTo { it.attribute }
}