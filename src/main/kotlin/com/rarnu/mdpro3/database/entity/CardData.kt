package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity

interface CardData : Entity<CardData> {
    companion object : Entity.Factory<CardData>()

    var id: Long
    var ot: Int
    var alias: Long
    var setcode: Long
    var type: Long
    var atk: Int
    var def: Int
    var level: Int
    var race: Long
    var attribute: Long
    var category: Long
    var genre: Long
    var script: ByteArray
    var support: Long
    var ocgdate: Long
    var tcgdate: Long

}