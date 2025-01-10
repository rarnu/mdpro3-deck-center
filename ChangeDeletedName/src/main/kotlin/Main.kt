package com.rarnu.mdpro3

import com.rarnu.mdpro3.api.DeckAPI
import com.rarnu.mdpro3.database.DatabaseManager
import com.rarnu.mdpro3.database.DatabaseManager.dbMDPro3

fun main() {
    DatabaseManager.initDatabase()
    // 获取已删除的所有卡组 id
    val deleted = DeckAPI.getDeletedDecks()

    dbMDPro3.useTransaction { trans ->

    }
}