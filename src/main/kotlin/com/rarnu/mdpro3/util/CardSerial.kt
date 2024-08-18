package com.rarnu.mdpro3.util

import com.rarnu.mdpro3.database.DatabaseManager.dbOmega
import com.rarnu.mdpro3.database.table.LANG_ZHCN
import com.rarnu.mdpro3.database.table.cardTexts
import org.ktorm.dsl.inList
import org.ktorm.entity.filter
import org.ktorm.entity.map

object CardSerial {

    fun getCardSerial(ids: List<Long>): String = dbOmega.cardTexts(LANG_ZHCN).filter { it.id inList ids }.map { it.name }.joinToString("")

}