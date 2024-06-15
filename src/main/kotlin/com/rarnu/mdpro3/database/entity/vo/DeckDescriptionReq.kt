package com.rarnu.mdpro3.database.entity.vo

data class DeckDescriptionReq(
    val userId: Long = 0L,
    val deckId: String = "",
    val description: String = ""
)
