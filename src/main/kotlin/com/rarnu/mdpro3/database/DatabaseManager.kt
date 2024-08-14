@file:Suppress("DuplicatedCode")

package com.rarnu.mdpro3.database

import com.alibaba.druid.pool.DruidDataSourceFactory
import com.isyscore.kotlin.common.join
import io.ktor.server.application.*
import org.ktorm.database.Database
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.ktorm.support.mysql.MySqlDialect
import org.ktorm.support.sqlite.SQLiteDialect

object DatabaseManager {

    lateinit var dbMDPro3: Database
    lateinit var dbNameAPI: Database
    lateinit var dbOmega: Database
    lateinit var dbRushDuelJP: Database
    lateinit var dbRushDuelCN: Database

    fun initMDPro3(info: Triple<String, String, String>) {
        val dataSource = DruidDataSourceFactory.createDataSource(
            mapOf(
                "driverClassName" to "com.mysql.cj.jdbc.Driver",
                "url" to info.first,
                "username" to info.second,
                "password" to info.third,
                "validationQuery" to "select 1"
            )
        )
        dbMDPro3 = Database.connect(dataSource = dataSource, dialect = MySqlDialect(), logger = ConsoleLogger(LogLevel.WARN))
    }

    fun initNameAPI(info: Triple<String, String, String>) {
        val dataSource = DruidDataSourceFactory.createDataSource(
            mapOf(
                "driverClassName" to "com.mysql.cj.jdbc.Driver",
                "url" to info.first,
                "username" to info.second,
                "password" to info.third,
                "validationQuery" to "select 1"
            )
        )
        dbNameAPI = Database.connect(dataSource = dataSource, dialect = MySqlDialect(), logger = ConsoleLogger(LogLevel.WARN))
    }

    fun initOmega(info: String) {
        val dataSource = DruidDataSourceFactory.createDataSource(
            mapOf(
                "driverClassName" to "org.sqlite.JDBC",
                "url" to info,
                "validationQuery" to "select 1"
            )
        )
        dbOmega = Database.connect(dataSource = dataSource, dialect = SQLiteDialect(), logger = ConsoleLogger(LogLevel.WARN))
    }

    fun initRushDuel(info: Pair<String, String>) {
        val dataSourceJp = DruidDataSourceFactory.createDataSource(
            mapOf(
                "driverClassName" to "org.sqlite.JDBC",
                "url" to info.first,
                "validationQuery" to "select 1"
            )
        )
        val dataSourceCn = DruidDataSourceFactory.createDataSource(
            mapOf(
                "driverClassName" to "org.sqlite.JDBC",
                "url" to info.second,
                "validationQuery" to "select 1"
            )
        )
        dbRushDuelJP = Database.connect(dataSource = dataSourceJp, dialect = SQLiteDialect(), logger = ConsoleLogger(LogLevel.WARN))
        dbRushDuelCN = Database.connect(dataSource = dataSourceCn, dialect = SQLiteDialect(), logger = ConsoleLogger(LogLevel.WARN))

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

    fun Application.readDatabaseRushDuelJPConfig(): Pair<String, String> {
        val jp = environment.config.propertyOrNull("database.rushduel.jdbcUrlJp")?.getString() ?: ""
        val cn = environment.config.propertyOrNull("database.rushduel.jdbcUrlCn")?.getString() ?: ""
        return jp join cn
    }

}