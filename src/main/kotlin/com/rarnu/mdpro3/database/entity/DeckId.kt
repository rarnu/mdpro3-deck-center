package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity

interface DeckId: Entity<DeckId> {

    companion object: Entity.Factory<DeckId>()

    var id: String

}