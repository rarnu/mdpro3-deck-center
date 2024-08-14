package com.rarnu.mdpro3.request

data class DeckPublicReq(
    val userId: Long = 0L,
    val deckId: String = "",
    val isPublic: Boolean = true
)
