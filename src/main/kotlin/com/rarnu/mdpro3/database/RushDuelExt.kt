package com.rarnu.mdpro3.database

import com.rarnu.mdpro3.database.DatabaseManager.dbOmega
import com.rarnu.mdpro3.database.Omega.one
import com.rarnu.mdpro3.database.table.cardTexts
import com.rarnu.mdpro3.request.YdkFindReq
import com.rarnu.mdpro3.request.YdkNamesReq
import com.rarnu.mdpro3.response.CardDataVO
import com.rarnu.mdpro3.response.CardNameVO
import com.rarnu.mdpro3.util.narrow
import com.rarnu.mdpro3.util.toDBStr
import com.rarnu.mdpro3.util.widen
import org.ktorm.dsl.*
import org.ktorm.entity.*

object RushDuel {

    fun rdkFindCardNameList(req: YdkFindReq): List<CardNameVO> =
        dbOmega.cardTexts(req.lang).filter { ((if (req.byEffect) it.name else it.desc) like "%${req.key.toDBStr()}%") and (it.id between 120000000L..120999999L) }
            .take(100).map { CardNameVO(it.id, Spec.modifyName(req.lang, it.id, it.name)) }

    fun rdkNamesByIds(req: YdkNamesReq): List<CardNameVO> {
        if (req.ids.isEmpty()) return listOf()
        return dbOmega.cardTexts(req.lang).filter { it.id inList req.ids }.map { CardNameVO(it.id, Spec.modifyName(req.lang, it.id, it.name)) }
    }

    fun rushCardNameList(name: String, lang: String): List<CardNameVO> =
        dbOmega.cardTexts(lang).filter { ((it.name like "%${name}%") or (it.name like "%${name.narrow()}%") or (it.name like "%${name.widen()}%")) and (it.id between 120000000L..120999999L) }
            .take(10).map { CardNameVO(it.id, Spec.modifyName(lang, it.id, it.name)) }

    fun random(lang: String): CardDataVO? {
        val id = dbOmega.cardTexts(lang).filter { (it.id gte 120000000L) and (it.id lte 120999999L) }.sortedBy { org.ktorm.support.sqlite.random() }.firstOrNull()?.id ?: 0
        return one(id, lang)
    }
}