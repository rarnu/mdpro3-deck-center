package com.rarnu.mdpro3.util

object SnowFlakeManager {

    /**
     * 起始的时间戳
     */
    private const val START_STAMP = 1480166465631L

    /**
     * 每一部分占用的位数
     */
    private const val SEQUENCE_BIT: Int = 12    // 序列号占用的位数
    private const val MACHINE_BIT: Int = 5      // 机器标识占用的位数
    private const val DATA_CENTER_BIT: Int = 5  // 数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private const val MAX_DATA_CENTER_NUM = (-1L shl DATA_CENTER_BIT).inv()
    private const val MAX_MACHINE_NUM = (-1L shl MACHINE_BIT).inv()
    private const val MAX_SEQUENCE = (-1L shl SEQUENCE_BIT).inv()

    /**
     * 每一部分向左的位移
     */
    private const val MACHINE_LEFT = SEQUENCE_BIT
    private const val DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT
    private const val TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT

    private lateinit var sf: SnowFlake

    fun initSnowFlake() {
        val dcId = 9L // environment.config.propertyOrNull("cluster.dataCenterId")?.getString()?.toLongOrNull() ?: 2L
        val mcId = 9L // environment.config.propertyOrNull("cluster.machineId")?.getString()?.toLongOrNull() ?: 1L
        sf = SnowFlake(dcId, mcId)
    }

    fun nextSnowId(): Long = sf.nextId()

    class SnowFlake(private val dataCenterId: Long, private val machineId: Long) {
        private var sequence = 0L   // 序列号
        private var lastStamp = -1L  // 上一次时间戳
        private val nextMill: Long
            get() {
                var mill = newStamp
                while (mill <= lastStamp) {
                    mill = newStamp
                }
                return mill
            }

        private val newStamp: Long get() = System.currentTimeMillis()

        init {
            if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
                throw IllegalArgumentException("dataCenterId can't be greater than MAX_DATA_CENTER_NUM or less than 0")
            }
            if (machineId > MAX_MACHINE_NUM || machineId < 0) {
                throw IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0")
            }
        }

        /**
         * 产生下一个ID
         */
        @Synchronized
        fun nextId(): Long {
            var currStamp = newStamp
            if (currStamp < lastStamp) {
                throw RuntimeException("Clock moved backwards. Refusing to generate id")
            }
            if (currStamp == lastStamp) {
                // 相同毫秒内，序列号自增
                sequence = sequence + 1 and MAX_SEQUENCE
                // 同一毫秒的序列数已经达到最大
                if (sequence == 0L) {
                    currStamp = nextMill
                }
            } else {
                // 不同毫秒内，序列号置为0
                sequence = 0L
            }
            lastStamp = currStamp
            return (currStamp - START_STAMP shl TIMESTAMP_LEFT or (dataCenterId shl DATA_CENTER_LEFT) or (machineId shl MACHINE_LEFT) or sequence)
        }
    }
}

