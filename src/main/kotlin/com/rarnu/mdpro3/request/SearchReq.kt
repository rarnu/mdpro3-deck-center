package com.rarnu.mdpro3.request

import com.rarnu.mdpro3.util.DataConvert.strToAttribute
import com.rarnu.mdpro3.util.DataConvert.strToCardType
import com.rarnu.mdpro3.util.DataConvert.strToIcon
import com.rarnu.mdpro3.util.DataConvert.strToMonsterType
import com.rarnu.mdpro3.util.DataConvert.strToRace
import com.rarnu.mdpro3.util.DataConvert.strToSubType

data class SearchReq(
    val key: String = "",
    val cardtype: String = "",
    val attribute: String = "",
    val icon: String = "",
    val subtype: String = "",
    val race: String = "",
    val monstertype: String = "",
    val lang: String = ""
)

data class SearchOriginReq(
    val key: String = "",
    val cardType: Long = 0L,
    val attribute: Int = 0,
    val icon: Long = 0L,
    val subType: Long = 0L,
    val race: Long = 0L,
    val monsterType: Long = 0L,
    val lang: String = ""
)

fun SearchReq.toOrigin(): SearchOriginReq = SearchOriginReq(
    key = key,
    cardType = strToCardType(cardtype),
    attribute = strToAttribute(attribute),
    icon = strToIcon(icon),
    subType = strToSubType(subtype),
    race = strToRace(race),
    monsterType = strToMonsterType(monstertype),
    lang = lang
)