package com.rarnu.mdpro3.util

import com.isyscore.kotlin.common.HttpMethod
import com.isyscore.kotlin.common.http
import com.isyscore.kotlin.common.toJson
import com.isyscore.kotlin.common.toObj

object MCTokenValidation {

    const val BASE_URL = "https://sapi.moecube.com:444"
    const val API_ACC_SIGNIN = "/accounts/signin"
    const val API_ACC_AUTHUSER = "/accounts/authUser"

    data class MCUSer(
        val id: Long = 0L,
        val username: String = "",
        val name: String = "",
        val email: String = "",
        val avatar: String = ""
    )

    data class MCUserWithToken(
        val user: MCUSer = MCUSer(),
        val token: String = ""
    )

    data class MCSignInReq(
        val account: String,
        val password: String
    )

    fun login(account: String, password: String): MCUserWithToken {
        val jsonRet = http {
            url = "$BASE_URL$API_ACC_SIGNIN"
            method = HttpMethod.POST
            mimeType = "application/json"
            headers["origin"] = "https://accounts.moecube.com"
            headers["referer"] = "https://accounts.moecube.com"
            headers["sec-fetch-dest"] = "empty"
            headers["sec-fetch-mode"] = "cors"
            headers["sec-fetch-site"] = "same-site"
            data = MCSignInReq(account, password).toJson()
        } ?: "{}"
        return jsonRet.toObj<MCUserWithToken>()
    }

    fun validate(token: String, userId: Long): Boolean {
        val jsonRet = http {
            url = "$BASE_URL$API_ACC_AUTHUSER"
            method = HttpMethod.GET
            headers["Authorization"] = "Bearer $token"
        } ?: "{}"
        val user = jsonRet.toObj<MCUSer>()
        return user.id == userId
    }

}