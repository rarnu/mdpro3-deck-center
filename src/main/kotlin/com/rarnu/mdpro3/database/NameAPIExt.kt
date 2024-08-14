@file:Suppress("DuplicatedCode")

package com.rarnu.mdpro3.database

import com.rarnu.mdpro3.database.DatabaseManager.dbNameAPI
import com.rarnu.mdpro3.database.table.cardNameTexts
import com.rarnu.mdpro3.database.table.setNameTexts
import com.rarnu.mdpro3.jp.effectCardNames
import com.rarnu.mdpro3.jp.kana
import com.rarnu.mdpro3.jp.normalKana
import com.rarnu.mdpro3.util.narrow
import com.rarnu.mdpro3.util.widen
import org.ktorm.dsl.eq
import org.ktorm.dsl.or
import org.ktorm.entity.find

object NameAPI {

    fun nameKanjiKana(name: String): String =
        dbNameAPI.cardNameTexts.find { (it.kanji eq name) or (it.kanji eq name.narrow()) or (it.kanji eq name.widen()) }?.kk ?: ""

    fun setKanjiKana(name: String): String =
        dbNameAPI.setNameTexts.find { (it.kanji eq name) or (it.kanji eq name.narrow()) or (it.kanji eq name.widen()) }?.kk ?: ""

    fun effectKanjiKana(name: String): String {
        val cn = effectCardNames(name).toMutableList()
        cn.sortByDescending { it.length }
        var e2 = name
        for (i in cn.indices) {
            e2 = e2.replace(cn[i], "{{$i}}")
        }
        e2 = kana(e2)
        for (i in cn.indices) {
            var isToken = false
            var tmp = cn[i]
            if (tmp.endsWith("トークン")) isToken = true
            var kk = nameKanjiKana(tmp)
            if (kk.isBlank()) {
                tmp = tmp.replace("トークン", "").trim()
                kk = setKanjiKana(tmp)
                if (kk.isBlank()) kk = kana(tmp)
            }
            if (isToken && !kk.endsWith("トークン")) kk += "トークン"
            e2 = e2.replace("{{$i}}", kk)
        }
        return e2
    }

    fun normalKanjiKana(name: String): String {
        val cn = effectCardNames(name).toMutableList()
        cn.sortByDescending { it.length }
        var e2 = name
        for (i in cn.indices) {
            e2 = e2.replace(cn[i], "{{$i}}")
        }
        e2 = normalKana(e2)
        for (i in cn.indices) {
            var isToken = false
            var tmp = cn[i]
            if (tmp.endsWith("トークン")) isToken = true
            var kk = nameKanjiKana(tmp)
            if (kk.isBlank()) {
                tmp = tmp.replace("トークン", "").trim()
                kk = setKanjiKana(tmp)
                if (kk.isBlank()) kk = kana(tmp)
            }
            if (isToken && !kk.endsWith("トークン")) kk += "トークン"
            e2 = e2.replace("{{$i}}", kk)
        }
        return e2
    }

}
