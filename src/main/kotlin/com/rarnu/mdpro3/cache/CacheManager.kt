@file:Suppress("UNCHECKED_CAST")

package com.rarnu.mdpro3.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit

object CacheManager {

    /**
     * 30 秒的缓存
     */
    private val cache: Cache<String, Any> = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.SECONDS).build()

    private val likeCache: Cache<String, Boolean> = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).build()

    operator fun<T: Any> get(key: String): T? = cache.getIfPresent(key) as? T

    operator fun<T: Any> set(key: String, value: T) {
        cache.put(key, value)
    }

    fun<T: Any> getOrNull(key: String, block: () -> T?): T? {
        var obj = cache.getIfPresent(key) as? T
        if (obj == null) {
            obj = block()
            if (obj != null) cache.put(key, obj)
        }
        return obj
    }

    fun<T: Any> get(key: String, block: () -> T): T = cache.get(key, block) as T

    fun hasLike(key: String): Boolean = likeCache.getIfPresent(key) == true

    fun accessLike(key: String) {
        likeCache.put(key, true)
    }
}