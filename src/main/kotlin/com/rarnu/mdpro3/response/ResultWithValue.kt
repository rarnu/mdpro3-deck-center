package com.rarnu.mdpro3.response

data class ResultWithValue<T>(var code: Int, var message: String, var messageValue: String, var data: T?) {
    companion object {
        @JvmStatic
        fun successNoData(code: Int = 0, message: String = ""): ResultWithValue<*> = ResultWithValue(code, message, "", null)
        @JvmStatic
        fun <T> success(code: Int = 0, message: String = "", data: T? = null): ResultWithValue<T> = ResultWithValue(code, message, "", data)
        @JvmStatic
        fun errorNoData(code: Int = 500, message: String = "", messageValue: String = ""): ResultWithValue<*> = ResultWithValue(code, message, messageValue,null)
        @JvmStatic
        fun <T> error(code: Int = 500, message: String = "", data: T?): ResultWithValue<T> = ResultWithValue(code, message, "", data)
    }
}