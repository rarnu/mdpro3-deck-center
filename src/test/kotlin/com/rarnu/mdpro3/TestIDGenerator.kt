package com.rarnu.mdpro3

import com.rarnu.mdpro3.util.IdGenerator
import kotlin.test.Test

class TestIDGenerator {

    @Test
    fun test() {
        for (i in 0..99) {
            println(IdGenerator.nextId())
        }
    }
}