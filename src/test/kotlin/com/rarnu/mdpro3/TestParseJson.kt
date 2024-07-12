package com.rarnu.mdpro3

import com.isyscore.kotlin.common.httpGet
import com.isyscore.kotlin.common.toObj
import kotlin.test.Test

class TestParseJson {

    @Test
    fun test() {
        val jsonStr = httpGet("https://sapi.moecube.com:444/release/update/apps.json").body
        val arr = jsonStr.toObj<List<Map<String, Any?>>>()
        val item = arr.find { it["id"] == "ygopro" }
        println(item)
    }

}