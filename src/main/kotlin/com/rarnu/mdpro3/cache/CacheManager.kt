@file:Suppress("UNCHECKED_CAST")

package com.rarnu.mdpro3.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit

object CacheManager {

    /**
     * 公开卡组缓存， 30 秒
     */
    private val publicCache: Cache<String, Any> = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.SECONDS).build()

    /**
     * 个人卡组同步缓存,15 秒
     */
    private val privateDeckCache: Cache<String, Any> = CacheBuilder.newBuilder().expireAfterWrite(15L, TimeUnit.SECONDS).build()

    /**
     * 单人点赞缓存，同IP点赞同卡组，10分钟一次
     */
    private val likeCache: Cache<String, Boolean> = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).build()

    fun <T : Any> getOrNull(key: String, isPublic: Boolean = true, block: () -> T?): T? {
        var obj = (if (isPublic) publicCache else privateDeckCache).getIfPresent(key) as? T
        if (obj == null) {
            obj = block()
            if (obj != null) (if (isPublic) publicCache else privateDeckCache).put(key, obj)
        }
        return obj
    }

    fun <T : Any> get(key: String, isPublic: Boolean = true, block: () -> T): T = (if (isPublic) publicCache else privateDeckCache).get(key, block) as T

    fun clean(key: String, isPublic: Boolean = true) {
        (if (isPublic) publicCache else privateDeckCache).invalidate(key)
    }

    fun hasLike(key: String): Boolean = likeCache.getIfPresent(key) == true

    fun accessLike(key: String) {
        likeCache.put(key, true)
    }
}