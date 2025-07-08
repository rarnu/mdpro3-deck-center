package com.rarnu.mdpro3.database.table

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.varchar


interface SensitiveWord: Entity<SensitiveWord> {

    companion object: Entity.Factory<SensitiveWord>()

    var word: String
}

object SensitiveWords : Table<SensitiveWord>("sensitive_word") {
    var word = varchar("word").primaryKey().bindTo { it.word }
}

val Database.sensitiveWords get() = this.sequenceOf(SensitiveWords)