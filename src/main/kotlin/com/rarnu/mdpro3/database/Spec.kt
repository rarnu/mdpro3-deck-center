package com.rarnu.mdpro3.database

import com.rarnu.mdpro3.database.table.LANG_EN
import com.rarnu.mdpro3.database.table.LANG_JP
import com.rarnu.mdpro3.database.table.LANG_ZHTW

object Spec {

    fun modifyName(lang: String, id: Long, name: String): String {
        var tmp = name.replace("&#64025;", "神")
        if (lang == LANG_ZHTW) {
            tmp = tmp.substringBeforeLast("【")
        }
        return tmp
    }

    /**
     * 特殊变更描述文字
     */
    fun modifyDesc(lang: String, id: Long, typ: Long, desc: String): String {
        var tmp = desc.replace("&#64025;", "神")
        if (lang == LANG_JP && id == 67616300L) {
            // 日语版试胆竞速
            return tmp.replace("\n", "")
        }
        if (lang == LANG_EN) {
            tmp = tmp.replace("'''", "")
        }
        if (lang == LANG_ZHTW) {
            val isPendulum = (typ and 0x1000000L) != 0L
            if (isPendulum) {
                tmp = modifyPendulumDesc(lang, id, desc)
            }
        }
        return tmp
    }

    fun modifyPendulumDesc(lang: String, id: Long, desc: String): String {
        if (lang == LANG_ZHTW) {
            val sl = desc.split("\n").filter { it.trim() != "" }.toMutableList()
            sl.removeAt(1)
            sl.removeAt(1)
            val newDesc = sl.joinToString("\n") { it.replace("\r", "") }
            return newDesc.replace("【怪獸效果】", "【Monster Effect】")
        }
        return desc
    }

}