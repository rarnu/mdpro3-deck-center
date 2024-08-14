package com.rarnu.mdpro3.util

import com.isyscore.kotlin.common.*
import com.isyscore.kotlin.common.HttpMethod
import java.io.Serializable
import java.util.UUID

object Translate {

    private const val YOUDAO_URL = "https://openapi.youdao.com/api"
    private const val APP_KEY    = "4a163f5a6c14ce0e"
    private const val APP_SECRET = "cv5jn949S9nekKm2Ozbz3tqVh2yiYjsT"

    data class TransQuery(
        val q: String = "",
        val from: String = "",
        val to: String = "",
        val appKey: String = "",
        val salt: String = "",
        val sign: String = "",
        val signType: String = "",
        val curtime: String = ""
    ): Serializable

    data class TransResult(
        val errorCode: String = "",
        val query: String = "",
        val translation: List<String> = listOf()
    )

    fun translate(content: String): String {
        val salt = UUID.randomUUID().toString()
        val tm = System.currentTimeMillis().toString().dropLast(3)
        val m = TransQuery(content, "zh-CHS", "ja", APP_KEY, salt, sign(content, salt, tm), "v3", tm)
        val body = http {
            url = YOUDAO_URL
            method = HttpMethod.POST
            postParam = m.toJson().toObj<MutableMap<String, String>>()
        }
        if (body == null) return ""
        val data = body.toObj<TransResult>()
        var str = data.translation.joinToString("") { it }
        translateMap.forEach { (k, v) ->
            str = str.replace(k, v)
        }
        return str
    }

    private fun sign(content: String, salt: String, tm: String): String {
        val input = if (content.length > 20) {
            content.take(10) + content.length + content.takeLast(10)
        } else content
        val str = "${APP_KEY}${input}${salt}${tm}${APP_SECRET}"
        return str.hash("SHA-256")
    }

    lateinit var translateMap: Map<String, String>

    fun initTranslateData() {
        translateMap = TRANSLATE_JSON.toObj<Map<String, String>>()
    }
}