package com.rarnu.mdpro3.request

data class TranslateReq(
    val query: String = "",
    val kk: Boolean = false,
    val kkMode: String = "" // 可选值为 effect/normal
)
