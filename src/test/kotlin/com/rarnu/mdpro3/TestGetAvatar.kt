package com.rarnu.mdpro3

import com.isyscore.kotlin.common.download
import kotlin.test.Test

class TestGetAvatar {

    @Test
    fun test() {
        download {
            url = "https://cdn02.moecube.com:444/avatars/cd0a1620-63a7-11ea-b940-e750faceb079.png"
            localFile = "a.png"
        }
    }

}