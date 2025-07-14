package com.rarnu.mdpro3

import com.rarnu.mdpro3.database.table.Puzzle
import org.junit.Test

class TestKtorm {



    @Test
    fun test() {
        val entity = Puzzle {
            id = 5L
            name = "6666"
        }

        println(entity::class.java.declaredFields)
        entity.properties
        println(entity::class.java)
        entity["id"] = null
        // entity.properties.toMutableMap().remove("id")
        println(entity)
    }

}