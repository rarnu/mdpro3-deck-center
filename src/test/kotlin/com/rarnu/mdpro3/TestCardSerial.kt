package com.rarnu.mdpro3

import com.rarnu.mdpro3.util.CardSerial
import org.junit.Test

class TestCardSerial {

    @Test
    fun test(){
        val str = CardSerial.getCardSerial(listOf(2511, 10000, 27551))
        println(str)
    }

    @Test
    fun test1() {
        val str = CardSerial.getCardSerial(listOf(2511, 10000))
        println(str)
    }

    @Test
    fun test2() {
        val str = CardSerial.getCardSerial(listOf())
        println(str)
    }

}