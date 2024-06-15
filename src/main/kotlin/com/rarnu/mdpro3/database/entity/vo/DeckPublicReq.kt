package com.rarnu.mdpro3.database.entity.vo

data class DeckPublicReq(
    val userId: Long = 0L,
    val deckId: String = "",
    val isPublic: Boolean = true
)
