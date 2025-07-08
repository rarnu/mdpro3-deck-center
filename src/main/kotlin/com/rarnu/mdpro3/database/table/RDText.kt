package com.rarnu.mdpro3.database.table

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

interface RDText: Entity<RDText> {
    companion object: Entity.Factory<RDText>()

    var id: Long
    var name: String
    var kk: String
    var desc: String
    var type: Long
    var atk: Int
    var def: Int
    var level: Int
    var race: Long
    var attribute: Long

}

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