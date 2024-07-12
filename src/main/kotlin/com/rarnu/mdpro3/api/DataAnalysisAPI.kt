package com.rarnu.mdpro3.api

import com.isyscore.kotlin.common.toJson
import com.rarnu.mdpro3.database.DatabaseManager.db
import com.rarnu.mdpro3.database.entity.vo.DauVO
import com.rarnu.mdpro3.ext.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.isyscore.kotlin.ktor.Result


fun Route.dataAnalysisAPI() = route("/analysis") {

    get("/dau") {
        val src = call.validateDauSrc() ?: return@get
        val year = call.validateDauYear() ?: return@get
        val month = call.validateDauMonth() ?: return@get
        val list = db.useConnection { conn ->
            conn.prepareStatement(SQL_DAU).use { stmt ->
                stmt.setString(1, src)
                stmt.setInt(2, year)
                stmt.setInt(3, month)
                stmt.executeQuery().useMap {
                    createEntity<DauVO>(it)
                }
            }
        }
        println(list.toJson())

        call.respond(Result.success(data = list))
    }
}

private const val SQL_DAU = """
select date(create_time) as 'dauDate', count(ip_address) as 'dauCount' from client_source
where source = ? and year(create_time) = ? and month(create_time) = ?
group by date(create_time);
"""