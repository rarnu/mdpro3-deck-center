package com.rarnu.mdpro3.database.table

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

interface CardText: Entity<CardText> {

    companion object: Entity.Factory<CardText>()

    var id: Long
    var name: String
    var desc: String

}

open class CardTexts(n: String): Table<CardText>(n) {

    var id = long("id").primaryKey().bindTo { it.id }
    var name = varchar("name").bindTo { it.name }
    var desc = varchar("desc").bindTo { it.desc }
}

const val LANG_ZHCN = "sc"
const val LANG_ZHTW = "tc"
const val LANG_JP = "jp"
const val LANG_EN = "en"
const val LANG_KR = "kr"

object ZhcnTexts: CardTexts("zhcn_texts")
object ZhtwTexts: CardTexts("zhtw_texts")
object JpTexts: CardTexts("ja_texts")
object EnTexts: CardTexts("texts")
object KrTexts: CardTexts("ko_texts")

val LANGUAGES: Map<String, CardTexts> = mapOf(
    LANG_ZHCN to ZhcnTexts,
    LANG_ZHTW to ZhtwTexts,
    LANG_JP to JpTexts,
    LANG_EN to EnTexts,
    LANG_KR to KrTexts
)

fun Map<String, CardTexts>.getOrDef(lang: String): CardTexts = LANGUAGES[lang] ?: JpTexts

fun Database.cardTexts(lang: String): EntitySequence<CardText, CardTexts> = sequenceOf(LANGUAGES[lang] ?: JpTexts)
