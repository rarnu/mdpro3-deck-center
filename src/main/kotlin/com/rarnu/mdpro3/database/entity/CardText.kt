package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity

interface CardText: Entity<CardText> {

    companion object: Entity.Factory<CardText>()

    var id: Long
    var name: String
    var desc: String

}