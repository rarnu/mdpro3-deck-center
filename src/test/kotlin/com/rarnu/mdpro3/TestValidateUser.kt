package com.rarnu.mdpro3

import com.rarnu.mdpro3.util.MCTokenValidation
import kotlin.test.Test

class TestValidateUser {

    @Test
    fun test() {
        val user = MCTokenValidation.login("rarnu", "Rarnu1120")
        println(user)
    }

    @Test
    fun test1() {
        val v = MCTokenValidation.validate("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDE4NjgsImlhdCI6MTc1MTk3MDYwMywiZXhwIjoxNzgzNTA2NjAzfQ.Q7PfmxXi3VB2h2MLPS_CjOSkxFcg-uG22yBRN5zloMg", 41868)
        println(v)
    }

}