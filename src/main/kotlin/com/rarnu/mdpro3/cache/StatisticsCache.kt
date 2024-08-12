package com.rarnu.mdpro3.cache

import com.rarnu.mdpro3.database.DatabaseManager.db
// import com.rarnu.mdpro3.database.entity.ClientSource
// import com.rarnu.mdpro3.database.table.ClientSources
import org.ktorm.dsl.batchInsert
import java.time.LocalDateTime

/*
object StatisticsCache {

    private val source = mutableListOf<ClientSource>()

    fun addSource(ip: String, src: String, ua: String, api: String) {
        val data = ClientSource {
            userAgent = ua
            source = src
            ipAddress = ip
            callApi = api
            createTime = LocalDateTime.now()
        }
        source.add(data)
        submitSources()
    }

    @Synchronized
    private fun submitSources() {
        if (source.size >= 100) {
            try {
                db.batchInsert(ClientSources) {
                    source.forEach { s ->
                        item {
                            set(ClientSources.userAgent, s.userAgent)
                            set(ClientSources.source, s.source)
                            set(ClientSources.ipAddress, s.ipAddress)
                            set(ClientSources.callApi, s.callApi)
                            set(ClientSources.createTime, s.createTime)
                        }
                    }
                }
            } catch (e: Exception) {

            }
            source.clear()
        }
    }
}

 */