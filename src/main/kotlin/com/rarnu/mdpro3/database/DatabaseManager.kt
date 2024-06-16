package com.rarnu.mdpro3.database

import com.alibaba.druid.pool.DruidDataSourceFactory
import com.isyscore.kotlin.common.join
import io.ktor.server.application.*
import org.ktorm.database.Database
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.ktorm.support.mysql.MySqlDialect

object DatabaseManager {

    lateinit var db: Database

    fun init(jdbcUrl: String, user: String, password: String) {
        val dataSource = DruidDataSourceFactory.createDataSource(
            mapOf(
                "driverClassName" to "com.mysql.cj.jdbc.Driver",
                "url" to jdbcUrl,
                "username" to user,
                "password" to password,
                "logLevel" to "DEBUG",
                "validationQuery" to "select 1"
            )
        )
        db = Database.connect(dataSource = dataSource, dialect = MySqlDialect(), logger = ConsoleLogger(LogLevel.DEBUG))
    }

    fun Application.readDatabaseConfig(): Triple<String, String, String> {
        val jdbcUrl = environment.config.propertyOrNull("database.jdbcUrl")?.getString() ?: ""
        val user = environment.config.propertyOrNull("database.user")?.getString() ?: ""
        val password = environment.config.propertyOrNull("database.password")?.getString() ?: ""
        return jdbcUrl join user join password
    }

}