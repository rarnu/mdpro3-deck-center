package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity

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