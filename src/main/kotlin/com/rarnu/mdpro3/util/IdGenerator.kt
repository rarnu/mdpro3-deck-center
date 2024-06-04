package com.rarnu.mdpro3.util

import java.math.BigInteger
import java.security.SecureRandom

object IdGenerator {

    private const val ID_LEN = 6

    fun nextId(): String {
        val random = SecureRandom()
        val bytes = ByteArray(ID_LEN)
        random.nextBytes(bytes)
        return BigInteger(1, bytes).toString(32)
    }

}