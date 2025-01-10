package com.rarnu.mdpro3.database

import com.alibaba.druid.pool.DruidDataSource
import com.alibaba.druid.pool.DruidDataSourceFactory
import com.isyscore.kotlin.common.databasePoolOf
import org.ktorm.database.Database
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.ktorm.support.mysql.MySqlDialect

object DatabaseManager {

    lateinit var dbMDPro3: Database

    fun initDatabase() {

        val prop = mutableMapOf("driverClassName" to "com.mysql.cj.jdbc.Driver",
            "url" to "jdbc:mysql://127.0.0.1:3306/mdpro3?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Shanghai",
            "username" to "root",
            "password" to "rootroot",
            "validationQuery" to "select 1;"
        )
        val dataSource = DruidDataSourceFactory.createDataSource(prop) as DruidDataSource
        val db = Database.connect(dataSource = dataSource, dialect = MySqlDialect(), logger = ConsoleLogger(LogLevel.INFO))
        dbMDPro3 = db
    }

}