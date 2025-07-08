@file:Suppress("DuplicatedCode")

package com.rarnu.mdpro3.request

import com.rarnu.mdpro3.database.table.Deck
import com.rarnu.mdpro3.util.CardSerial
import java.time.Instant

/**
 * 同步多个卡组请求
 */
data class MultiSyncReq(
    var deckContributor: String = "",
    var userId: Long = 0L,
    var decks: List<SyncDeckReq> = listOf()
)

/**
 * 同步单个卡组请求
 */
data class SingleSyncReq(
    var deckContributor: String = "",
    var userId: Long = 0L,
    var deck: SyncDeckReq = SyncDeckReq()
)

data class SyncDeckReq(
    var deckId: String = "",
    var deckName: String = "",
    var deckCoverCard1: Long = 0L,
    var deckCoverCard2: Long = 0L,
    var deckCoverCard3: Long = 0L,
    var deckCase: Long = 0L,
    var deckProtector: Long = 0L,
    var deckYdk: String = "",
    var isDelete: Boolean = false
)

fun SyncDeckReq.toDeck(userId: Long, contributor: String = "", isUpdate: Boolean = false): Deck {
    val d = Deck {}
    d.deckId = this.deckId
    d.deckContributor = contributor
    d.deckName = this.deckName
    d.deckCoverCard1 = this.deckCoverCard1
    d.deckCoverCard2 = this.deckCoverCard2
    d.deckCoverCard3 = this.deckCoverCard3
    d.deckCase = this.deckCase
    d.deckProtector = this.deckProtector
    d.deckYdk = this.deckYdk
    d.userId = userId
    d.deckMainSerial = CardSerial.getCardSerial(listOf(deckCoverCard1, deckCoverCard2, deckCoverCard3))
    if (!isUpdate) {
        // 如果不是更新(是新增)，则写入 upload_date
        d.deckUploadDate = Instant.now()
    }
    // 任何情况都会写入 update_date
    d.deckUpdateDate = Instant.now()
    return d
}

