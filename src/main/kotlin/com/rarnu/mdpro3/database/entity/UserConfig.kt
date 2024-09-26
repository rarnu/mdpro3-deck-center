package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity

interface UserConfig: Entity<UserConfig> {
    companion object : Entity.Factory<UserConfig>()

    var userId: Long
    var config: String
    var extra1: String
    var extra2: String
    var extra3: String
    var extra4: String
    var extra5: String

}