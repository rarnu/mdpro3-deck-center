package com.rarnu.mdpro3.database

import com.rarnu.mdpro3.database.DatabaseManager.dbOmega
import com.rarnu.mdpro3.database.table.*
import com.rarnu.mdpro3.request.YdkFindReq
import com.rarnu.mdpro3.request.YdkNamesReq
import com.rarnu.mdpro3.response.CardDataVO
import com.rarnu.mdpro3.response.CardNameVO
import com.rarnu.mdpro3.util.narrow
import com.rarnu.mdpro3.util.widen
import org.ktorm.dsl.*
import org.ktorm.entity.*

object Omega {

    fun one(password: Long, lang: String): CardDataVO? {
        val t = LANGUAGES.getOrDef(lang)
        val data = dbOmega.from(t).leftJoin(CardDatas, on = t.id eq CardDatas.id)
            .select(t.id, t.name, t.desc, CardDatas.type, CardDatas.atk, CardDatas.def, CardDatas.level, CardDatas.race, CardDatas.attribute)
            .where { t.id eq password }
            .limit(1).map { CardDataVO.fromResultSet(it) }.firstOrNull()
        if (data == null) return null
        data.name = Spec.modifyName(lang, data.id, data.name)
        data.desc = Spec.modifyDesc(lang, data.id, data.type, data.desc)
        data.setid = "LWCG"
        return data
    }

    fun random(lang: String): CardDataVO? {
        val id = dbOmega.cardTexts(lang).filter { (it.id gte 10000) and (it.id lte 99999999) }.sortedBy { org.ktorm.support.sqlite.random() }.first().id
        return one(id, lang)
    }

    fun cardNameList(name: String, lang: String): List<CardNameVO> =
        dbOmega.cardTexts(lang).filter { (it.name like "%${name}%") or (it.name like "%${name.narrow()}%") or (it.name like "%${name.widen()}%") }
            .take(10).map { CardNameVO(it.id, Spec.modifyName(lang, it.id, it.name)) }


    fun ydkFindCardNameList(req: YdkFindReq): List<CardNameVO> =
        dbOmega.cardTexts(req.lang).filter { (if (req.byEffect) it.name else it.desc) like "%${req.key.toDBStr()}%" }
            .take(100).map { CardNameVO(it.id, Spec.modifyName(req.lang, it.id, it.name)) }

    fun ydkNamesByIds(req: YdkNamesReq): List<CardNameVO> {
        if (req.ids.isEmpty()) return listOf()
        return dbOmega.cardTexts(req.lang).filter { it.id inList req.ids }.map { CardNameVO(it.id, Spec.modifyName(req.lang, it.id, it.name)) }
    }

    fun String.toDBStr(): String = replace("'", "''").replace("\n", "").replace("\r", "")

}