package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity

interface SensitiveWord: Entity<SensitiveWord> {

    companion object: Entity.Factory<SensitiveWord>()

    var word: String
}