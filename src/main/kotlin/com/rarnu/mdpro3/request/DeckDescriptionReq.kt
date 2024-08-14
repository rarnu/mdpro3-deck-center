package com.rarnu.mdpro3.request

data class DeckDescriptionReq(
    val userId: Long = 0L,
    val deckId: String = "",
    val description: String = ""
)
