package com.rarnu.mdpro3.response

import java.time.LocalDateTime

data class PuzzleVO(
    var id: Long = 0L,
    var name: String = "",
    var userId: Long = 0L,
    var contributor: String = "",
    var message: String = "",
    var solution: String = "",
    var coverCard: Long = 0L,
    var publishDate: LocalDateTime = LocalDateTime.now(),
    var luaScript: String = "",
    var passedCount: Long = 0L
)