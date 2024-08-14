package com.rarnu.mdpro3

import com.isyscore.kotlin.common.HttpMethod
import com.isyscore.kotlin.common.http
import com.isyscore.kotlin.common.toObj
import com.rarnu.mdpro3.request.SingleSyncReq
import kotlin.test.Test

class TestSyncSingle {

    @Test
    fun test() {

        val obj = DATA.toObj<SingleSyncReq>()
        println(obj)

        http {
            url = "http://0.0.0.0:38383/api/mdpro3/sync/single"
            method = HttpMethod.POST
            headers["token"] = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NzU4MjQ2LCJpYXQiOjE3MTg0MjcxODIsImV4cCI6MTc0OTk2MzE4Mn0.Z_CAGLWTJ38ZzlPbPbGflCF6z1iNiJgFmPZumLaiuwM"
            headers[""]
            mimeType = "application/json"
            data = DATA
            // isHttps = true
            onSuccess { code, text, headers, cookie ->
                println("code = $code, text = $text")
            }
            onFail {
                println(it)
            }
        }
    }

}


private const val DATA = """
{
    "userId": 758246,
    "deckContributor": "sktt1ryze",
    "deck": {
        "deckId": "4vk1d6j6v9",
        "deckName": "风魔女歌冰-测试",
        "deckCoverCard1": 0,
        "deckCoverCard2": 0,
        "deckCoverCard3": 0,
        "deckCase": 1082012,
        "deckProtector": 0,
        "isDelete": false,
        "deckYdk": "#created by neos\n#main\n27204311\n36956512\n36956512\n36956512\n55063751\n29726552\n86395581\n86395581\n84851250\n84851250\n71007216\n71007216\n71007216\n20246864\n20246864\n20246864\n43722862\n43722862\n43722862\n14558127\n14558127\n14558127\n23434538\n23434538\n23434538\n70117860\n70117860\n70117860\n14532163\n14532163\n96156729\n96156729\n96156729\n99330325\n39049051\n39049051\n39049051\n1845204\n24094653\n24094653\n#extra\n25793414\n25793414\n76815942\n14577226\n14577226\n14577226\n35252119\n50954680\n50954680\n73667937\n84815190\n92519087\n98506199\n90036274\n30674956\n!side"
    }
}
"""

//