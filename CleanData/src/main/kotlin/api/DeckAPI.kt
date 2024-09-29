package com.rarnu.mdpro3.api

import com.rarnu.mdpro3.database.DatabaseManager.dbMDPro3
import com.rarnu.mdpro3.database.entity.Deck
import com.rarnu.mdpro3.database.table.Decks
import com.rarnu.mdpro3.database.table.decks
import org.ktorm.dsl.*
import org.ktorm.entity.removeIf

object DeckAPI {

    /**
     * 删除 user_id 为 0 的卡组
     */
    fun deleteUser0(): Int = dbMDPro3.decks.removeIf { it.userId eq 0L }


    /**
     * 获取全部用户的 id
     */
    fun getUserIds(): List<Long> = dbMDPro3.from(Decks).selectDistinct(Decks.userId).map { it.getLong(1) }


    /**
     * 获取用户对应的卡组分组
     */
    fun getDeckGroup(userId: Long): Map<String, List<Deck>> {
        val list = dbMDPro3.from(Decks).select().where {
            Decks.userId eq userId
        }.orderBy(Decks.deckName.asc(), Decks.deckUpdateDate.desc()).map { Decks.createEntity(it) }
        return list.groupBy { it.deckName.lowercase() }
    }

    /**
     * 删降名称重复的卡组
     */
    fun deleteUserDecks(decks: List<String>): Int = dbMDPro3.decks.removeIf { it.deckId inList decks }
}