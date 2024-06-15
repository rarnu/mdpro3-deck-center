package com.rarnu.mdpro3.database.entity.vo

import com.rarnu.mdpro3.database.table.Decks
import org.ktorm.dsl.QueryRowSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class DeckLiteVO(
    var deckId: String = "",
    var deckContributor: String = "",
    var deckName: String = "",
    var deckLike: Int = 0,
    var deckCoverCard1: Long = 0L,
    var deckCoverCard2: Long = 0L,
    var deckCoverCard3: Long = 0L,
    var deckCase: Long = 0L,
    var deckProtector: Long = 0L,
    var lastDate: String = "",
    var userId: Long = 0,
) {
    companion object
}

fun DeckLiteVO.Companion.fromRow(row: QueryRowSet): DeckLiteVO = DeckLiteVO(
    deckId = row[Decks.deckId] ?: "",
    deckContributor = row[Decks.deckContributor] ?: "",
    deckName = row[Decks.deckName] ?: "",
    deckLike = row[Decks.deckLike] ?: 0,
    deckCoverCard1 = row[Decks.deckCoverCard1] ?: 0L,
    deckCoverCard2 = row[Decks.deckCoverCard2] ?: 0L,
    deckCoverCard3 = row[Decks.deckCoverCard3] ?: 0L,
    deckCase = row[Decks.deckCase] ?: 0L,
    deckProtector = row[Decks.deckProtector] ?: 0L,
    lastDate =  (row[Decks.deckUpdateDate] ?: row[Decks.deckUploadDate] ?: LocalDateTime.now()).format(DateTimeFormatter.ISO_DATE),
    userId = row[Decks.userId] ?: 0L
)