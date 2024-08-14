package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity

interface SetNameText: Entity<SetNameText> {

    companion object: Entity.Factory<SetNameText>()

    var id: Long
    var en: String
    var kanji: String
    var kk: String
    var donetime: Long

}