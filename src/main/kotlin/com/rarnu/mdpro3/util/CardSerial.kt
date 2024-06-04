package com.rarnu.mdpro3.util

import com.isyscore.kotlin.common.HttpMethod
import com.isyscore.kotlin.common.http
import com.isyscore.kotlin.common.toObj
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

object CardSerial {

    fun getCardSerial(ids: List<Long>): String = runBlocking {
        ids.map { async { getCard(it) } }.awaitAll().joinToString("")
    }

    private fun getCard(id: Long): String = try {
        val jsonRet = http {
            url = "http://182.92.234.65:9800/api/yugioh/card/${id}?lang=sc"
            method = HttpMethod.GET
        } ?: "{}"
        val cn = jsonRet.toObj<CardName>()
        cn.name
    } catch (_: Exception) {
        ""
    }

    data class CardName(val name: String)

}