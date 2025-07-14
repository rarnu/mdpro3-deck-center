@file:Suppress("DuplicatedCode")

package com.rarnu.mdpro3.database


import com.isyscore.kotlin.common.databasePoolOf
import com.isyscore.kotlin.common.join
import io.ktor.server.application.*
import org.ktorm.database.Database
import org.ktorm.support.postgresql.PostgreSqlDialect
import org.ktorm.support.sqlite.SQLiteDialect

object DatabaseManager {

    lateinit var dbMDPro3: Database
    lateinit var dbNameAPI: Database
    lateinit var dbOmega: Database

    fun initMDPro3(info: Triple<String, String, String>) {
        val (db, err) = databasePoolOf(
            "org.postgresql.Driver", info.first, info.second, info.third, PostgreSqlDialect(),
            maxActive = 10000,
            connTimeout = 3000,
            queryTimeout = 3000,
            socketTimeout = 5000
        )
        if (err != null) throw err
        dbMDPro3 = db!!
    }

    fun initNameAPI(info: Triple<String, String, String>) {
        val (db, err) = databasePoolOf("org.postgresql.Driver", info.first, info.second, info.third, PostgreSqlDialect())
        if (err != null) throw err
        dbNameAPI = db!!
    }

    fun initOmega(info: String) {
        val (db, err) = databasePoolOf("org.sqlite.JDBC", info, "", "", SQLiteDialect())
        if (err != null) throw err
        dbOmega = db!!
    }

    fun Application.readDatabaseMDPro3Config(): Triple<String, String, String> {
        val jdbcUrl = environment.config.propertyOrNull("database.mdpro3.jdbcUrl")?.getString() ?: ""
        val user = environment.config.propertyOrNull("database.mdpro3.user")?.getString() ?: ""
        val password = environment.config.propertyOrNull("database.mdpro3.password")?.getString() ?: ""
        return jdbcUrl join user join password
    }

    fun Application.readDatabaseNameAPIConfig(): Triple<String, String, String> {
        val jdbcUrl = environment.config.propertyOrNull("database.nameapi.jdbcUrl")?.getString() ?: ""
        val user = environment.config.propertyOrNull("database.nameapi.user")?.getString() ?: ""
        val password = environment.config.propertyOrNull("database.nameapi.password")?.getString() ?: ""
        return jdbcUrl join user join password
    }

    fun Application.readDatabaseOmegaConfig(): String =
        environment.config.propertyOrNull("database.omega.jdbcUrl")?.getString() ?: ""

}
