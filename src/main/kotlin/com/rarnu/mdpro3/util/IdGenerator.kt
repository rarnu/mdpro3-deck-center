package com.rarnu.mdpro3.util

import com.rarnu.mdpro3.database.DatabaseManager.dbMDPro3
import com.rarnu.mdpro3.database.entity.DeckId
import com.rarnu.mdpro3.database.table.deckIds
import org.ktorm.entity.add
import java.math.BigInteger
import java.security.SecureRandom

object IdGenerator {

    private const val ID_LEN = 6

    fun nextId(): String {
        val random = SecureRandom()
        val bytes = ByteArray(ID_LEN)
        random.nextBytes(bytes)
        return BigInteger(1, bytes).toString(32).padStart(10, '0')
    }

    fun nextIdDB(): String {
        val nid = nextId()
        val ret = dbMDPro3.useTransaction { trans ->
            try {
                val added = dbMDPro3.deckIds.add(DeckId {
                    id = nid
                })
                trans.commit()
                added > 0
            } catch (e: Exception) {
                trans.rollback()
                false
            }
        }
        return if (!ret) nextIdDB() else nid
    }

}