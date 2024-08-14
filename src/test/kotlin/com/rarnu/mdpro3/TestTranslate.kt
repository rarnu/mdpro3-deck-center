package com.rarnu.mdpro3

import com.rarnu.mdpro3.util.Translate
import com.rarnu.mdpro3.util.Translate.initTranslateData
import kotlin.test.Test

class TestTranslate {

    @Test
    fun test() {
        initTranslateData()
        val str = Translate.translate("以高攻击力著称的传说之龙。任何对手都能将之粉碎，其破坏力不可估量。")
        println(str)
    }

}