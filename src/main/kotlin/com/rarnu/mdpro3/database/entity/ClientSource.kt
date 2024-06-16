package com.rarnu.mdpro3.database.entity

import org.ktorm.entity.Entity
import java.time.LocalDateTime

interface ClientSource: Entity<ClientSource> {

    companion object: Entity.Factory<ClientSource>()

    var id: Long
    var userAgent: String
    var source: String
    var ipAddress: String
    var callApi: String
    var createTime: LocalDateTime


}