package com.rarnu.mdpro3.database.entity.vo

import java.time.LocalDate

data class DauVO(
    val dauDate: LocalDate = LocalDate.now(),
    val dauCount: Long = 0L
)