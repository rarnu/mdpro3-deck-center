@file:Suppress("DuplicatedCode")

package com.rarnu.mdpro3.database

import com.alibaba.druid.pool.DruidDataSource
import com.alibaba.druid.pool.DruidDataSourceFactory
import com.isyscore.kotlin.common.join
import io.ktor.server.application.*
import org.ktorm.database.Database
import org.ktorm.database.SqlDialect
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.ktorm.support.mysql.MySqlDialect
import org.ktorm.support.sqlite.SQLiteDialect

object DatabaseManager {

    lateinit var dbMDPro3: Database
    lateinit var dbNameAPI: Database
    lateinit var dbOmega: Database

    fun initMDPro3(info: Triple<String, String, String>) {
        val (db, err) = databasePoolOf("com.mysql.cj.jdbc.Driver", info.first, info.second, info.third, MySqlDialect())
        if (err != null) throw err
        dbMDPro3 = db!!
    }

    fun initNameAPI(info: Triple<String, String, String>) {
        val (db, err) = databasePoolOf("com.mysql.cj.jdbc.Driver", info.first, info.second, info.third, MySqlDialect())
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

    fun Application.readDatabaseOmegaConfig(): String = environment.config.propertyOrNull("database.omega.jdbcUrl")?.getString() ?: ""


    private fun databasePoolOf(
        driverClass: String, jdbcUrl: String, user: String, password: String, dialect: SqlDialect,
        logLevel: LogLevel = LogLevel.INFO, validationQuery: String = "select 1;"
    ): Pair<Database?, Throwable?> {
        try {
            val prop = mutableMapOf("driverClassName" to driverClass,
                "url" to jdbcUrl,
                "username" to user,
                "password" to password,
                "logLevel" to logLevel.name.uppercase(),
                "validationQuery" to validationQuery
            )
            val dataSource = DruidDataSourceFactory.createDataSource(prop) as DruidDataSource
            val db = Database.connect(dataSource = dataSource, dialect = dialect, logger = ConsoleLogger(logLevel))
            return db to null
        } catch (e: Throwable) {
            return null to e
        }
    }
}