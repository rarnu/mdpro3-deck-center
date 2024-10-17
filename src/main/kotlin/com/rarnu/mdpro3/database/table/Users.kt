package com.rarnu.mdpro3.database.table

import com.rarnu.mdpro3.database.entity.User
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long

object Users: Table<User>("user") {
    var userId = long("user_id").primaryKey().bindTo { it.userId }
    var roleId = int("role_id").bindTo { it.roleId }
}

val Database.users get() = this.sequenceOf(Users)
