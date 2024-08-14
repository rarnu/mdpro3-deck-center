package com.rarnu.mdpro3.jp

import com.atilika.kuromoji.ipadic.Token
import com.atilika.kuromoji.ipadic.Tokenizer
import com.isyscore.kotlin.common.toObj

fun removeKana(str: String): String =
    "\\[.*?\\(.*?\\)]".toRegex().replace(str) { mr ->
        mr.value.replace("[", "").substringBefore("(")
    }

fun effectCardNames(str: String): List<String> =
    "「.*?」".toRegex().findAll(str).map { it.value.replace("「", "").replace("」", "") }.distinct().toList()

fun kana(str: String): String =
    "\\[.*?\\(.*?\\)]".toRegex().replace(str) { mr ->
        "|${mr.value}|"
    }.split("|").joinToString("") {
        val reg = "\\[.*?\\(.*?\\)]".toRegex()
        if (!reg.matches(it)) {
            kanjiKanaReg.replace(it) { mr ->
                kanjiKanaMap[mr.value] ?: ""
            }
        } else it
    }

const val hiragana = "ぁあぃいぅうぇえぉおかがきぎくぐけげこごさざしじすずせぜそぞただちぢっつづてでとどなにぬねのはばぱひびぴふぶぷへべぺほぼぽまみむめもゃやゅゆょよらりるれろゎわゐゑをん"
const val katakana = "ァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロヮワヰヱヲンヴヵヶ"

fun normalKana(str: String): String =
    Tokenizer().tokenize(str).map {
        it.kana()
    }.reduce { acc, s ->
        acc + s
    }

private fun Token.kana(): String {
    val kataKana = surface.toKataKana()
    if (kataKana == reading.toKataKana() || reading == "*") {
        return surface
    }
    var s1Ori = surface
    var s1 = surface.toHiragana()
    var h2 = reading.toHiragana()
    var s2: String
    var head = ""
    var tail = ""
    while (true) {
        s2 = h2
        if (s1[0] != s2[0]) break
        head += s1[0]
        s1Ori = s1Ori.drop(1)
        s1 = s1.drop(1)
        h2 = s2.drop(1)
    }
    while (s1[s1.lastIndex] == s2[s2.lastIndex]) {
        tail = s1[s1.lastIndex] + tail
        s1Ori = s1Ori.dropLast(1)
        s1 = s1.dropLast(1)
        s2 = s2.dropLast(1)
    }
    return "$head[$s1Ori($s2)]$tail"

}

private fun String.toKataKana(): String {
    var str = ""
    for (element in this) {
        val idx = hiragana.indexOf(element)
        str += if (idx != -1) katakana[idx] else element
    }
    return str
}

private fun String.toHiragana(): String {
    var str = ""
    for (element in this) {
        val idx = katakana.indexOf(element)
        str += if (idx != -1) hiragana[idx] else element
    }
    return str
}

lateinit var kanjiKanaMap: Map<String, String>
lateinit var kanjiKanaReg: Regex

fun initKanjikanaData() {
    kanjiKanaMap = KANJIKANA_JSON.toObj<Map<String, String>>()
    val keys = kanjiKanaMap.keys.toMutableList()
    keys.sortByDescending { it.length }
    val regStr = keys.joinToString("|") { it }
    kanjiKanaReg = regStr.toRegex()
}