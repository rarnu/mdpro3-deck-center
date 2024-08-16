package com.rarnu.mdpro3.response

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
)

