package com.rarnu.mdpro3

import com.rarnu.mdpro3.api.DeckAPI
import com.rarnu.mdpro3.database.DatabaseManager

fun main() {
    DatabaseManager.initDatabase()

    // 删除 user_id 为 0 的卡组
    val delCnt = DeckAPI.deleteUser0()
    println("已删除 user_id 为 0 的数据，共删除 $delCnt 条")

    // 获取全部用户的 id
    val users = DeckAPI.getUserIds()
    println("获取全部的用户 id，共 ${users.size} 个用户")

    users.forEach { uid ->
        val ug = DeckAPI.getDeckGroup(uid)
        val decksToDelete = mutableListOf<String>()
        for ((_, deckList) in ug) {
            if (deckList.size > 1) {
                decksToDelete.addAll(deckList.drop(1).map { it.deckId })
            }
        }
        println("获取属于用户 $uid 的要删除的重复卡组， 共 ${decksToDelete.size} 个卡组")
        if (decksToDelete.isNotEmpty()) {
            val del = DeckAPI.deleteUserDecks(decksToDelete)
            println("已删除用户 $uid 的重复卡组，共删除 $del 个卡组")
        }
    }
}