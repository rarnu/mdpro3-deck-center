package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity

interface CardNameText : Entity<CardNameText> {
    companion object : Entity.Factory<CardNameText>()

    var id: Long
    var kanji: String
    var kk: String
    var donetime: Long

}