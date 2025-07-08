package com.rarnu.mdpro3.database.table

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long

interface User: Entity<User> {
    companion object: Entity.Factory<User>()

    var userId: Long
    var roleId: Int
}

object Users: Table<User>("_user") {
    var userId = long("user_id").primaryKey().bindTo { it.userId }
    var roleId = int("role_id").bindTo { it.roleId }
}

val Database.users get() = this.sequenceOf(Users)
