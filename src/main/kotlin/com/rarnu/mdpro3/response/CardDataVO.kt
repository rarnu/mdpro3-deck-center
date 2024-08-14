package com.rarnu.mdpro3.response

import java.sql.ResultSet

data class CardDataVO(
    var id: Long = 0L,
    var name: String = "",
    var desc: String = "",
    var type: Long = 0L,
    var atk: Int = 0,
    var def: Int = 0,
    var level: Int = 0,
    var race: Long = 0L,
    var attribute: Int = 0,
    var setid: String = ""
) {
    companion object {
        fun fromResultSet(rs: ResultSet): CardDataVO = CardDataVO(
            rs.getLong(1),
            rs.getString(2),
            rs.getString(3),
            rs.getLong(4),
            rs.getInt(5),
            rs.getInt(6),
            rs.getInt(7),
            rs.getLong(8),
            rs.getInt(9),
            "LWCG"
        )
    }
}

