package com.rarnu.mdpro3.database.table

// import com.rarnu.mdpro3.database.entity.ClientSource
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.long
import org.ktorm.schema.varchar

//object ClientSources : Table<ClientSource>("client_source") {
//
//    var id = long("id").primaryKey().bindTo { it.id }
//    var userAgent = varchar("user_agent").bindTo { it.userAgent }
//    var source = varchar("source").bindTo { it.source }
//    var ipAddress = varchar("ip_address").bindTo { it.ipAddress }
//    var callApi = varchar("call_api").bindTo { it.callApi }
//    var createTime = datetime("create_time").bindTo { it.createTime }
//
//}

// val Database.clientSources get() = this.sequenceOf(ClientSources)