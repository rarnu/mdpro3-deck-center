package com.rarnu.mdpro3.database.table

import com.rarnu.mdpro3.database.entity.SensitiveWord
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.varchar

object SensitiveWords : Table<SensitiveWord>("sensitive_word") {

    var word = varchar("word").primaryKey().bindTo { it.word }

}

val Database.sensitiveWords get() = this.sequenceOf(SensitiveWords)