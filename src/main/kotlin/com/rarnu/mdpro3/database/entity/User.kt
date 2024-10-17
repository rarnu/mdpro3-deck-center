package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity

interface User: Entity<User> {
    companion object: Entity.Factory<User>()

    var userId: Long
    var roleId: Int
}