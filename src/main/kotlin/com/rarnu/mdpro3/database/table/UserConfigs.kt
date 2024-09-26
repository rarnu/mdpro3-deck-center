package com.rarnu.mdpro3.database.table

import com.rarnu.mdpro3.database.entity.UserConfig
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.text

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