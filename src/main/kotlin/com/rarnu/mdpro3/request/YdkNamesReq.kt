package com.rarnu.mdpro3.request

data class YdkNamesReq(
    var lang: String = "",
    var ids: List<Long> = listOf()
)
