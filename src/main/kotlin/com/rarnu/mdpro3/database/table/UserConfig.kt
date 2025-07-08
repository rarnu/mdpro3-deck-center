package com.rarnu.mdpro3.database.table

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.text

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

object UserConfigs : Table<UserConfig>("user_config") {
    var userId = long("user_id").primaryKey().bindTo { it.userId }
    var config = text("config").bindTo { it.config }
    var extra1 = text("extra1").bindTo { it.extra1 }
    var extra2 = text("extra2").bindTo { it.extra2 }
    var extra3 = text("extra3").bindTo { it.extra3 }
    var extra4 = text("extra4").bindTo { it.extra4 }
    var extra5 = text("extra5").bindTo { it.extra5 }
}

val Database.userConfigs get() = this.sequenceOf(UserConfigs)