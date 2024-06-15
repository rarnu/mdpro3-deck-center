@file:Suppress("UNCHECKED_CAST")

package com.rarnu.mdpro3.ext

import com.isyscore.kotlin.common.toTitleUpperCase
import org.ktorm.entity.*
import org.ktorm.schema.*
import java.lang.reflect.Field
import java.math.BigDecimal
import java.sql.ResultSet
import java.sql.Time
import java.sql.Timestamp
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.temporal.ChronoField
import java.util.*
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

fun <E : Entity<E>, T : Table<E>> EntitySequence<E, T>.save(overrideExisting: Boolean, entity: E, predicate: (T) -> ColumnDeclaring<Boolean>): Int {
    val e = find(predicate)
    return if (e == null) {
        // 如果数据不存在，新增它
        add(entity)
    } else {
        // 如果数据已存在
        if (overrideExisting) {
            // 判断是否要覆盖
            update(entity)
        } else {
            // 不覆盖时直接返回 0
            0
        }
    }
}

fun <E : Entity<E>> BaseTable<E>.createEntity(row: ResultSet): E {
    val entityClass = this.entityClass ?: error("No entity class configured for table: '$this'")
    val entity = Entity.create(entityClass) as E
    for (column in columns) {
        row.retrieveColumn(column, intoEntity = entity)
    }
    return entity
}

val formatterMonthDay: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendValue(ChronoField.MONTH_OF_YEAR, 2)
    .appendLiteral('-')
    .appendValue(ChronoField.DAY_OF_MONTH, 2)
    .toFormatter()
val formatterYearMonth: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
    .appendLiteral('-')
    .appendValue(ChronoField.MONTH_OF_YEAR, 2)
    .toFormatter()

fun <E : Entity<E>> ResultSet.retrieveColumn(column: Column<*>, intoEntity: E) {
    val binding = column.binding ?: return

    val propName = when (binding) {
        is ReferenceBinding -> binding.onProperty.name
        is NestedBinding -> if (binding.properties.isEmpty()) return else binding.properties.first().name
    }

    when (column.sqlType) {
        BooleanSqlType -> intoEntity[propName] = this.getBoolean(column.name)
        IntSqlType -> intoEntity[propName] = this.getInt(column.name)
        ShortSqlType -> intoEntity[propName] = this.getShort(column.name)
        LongSqlType -> intoEntity[propName] = this.getLong(column.name)
        FloatSqlType -> intoEntity[propName] = this.getFloat(column.name)
        DoubleSqlType -> intoEntity[propName] = this.getDouble(column.name)
        DecimalSqlType -> intoEntity[propName] = this.getBigDecimal(column.name)
        VarcharSqlType -> intoEntity[propName] = this.getString(column.name)
        BytesSqlType -> intoEntity[propName] = this.getBytes(column.name)
        TimestampSqlType -> intoEntity[propName] = this.getTimestamp(column.name)
        DateSqlType -> intoEntity[propName] = this.getDate(column.name)
        TimeSqlType -> intoEntity[propName] = this.getTime(column.name)
        InstantSqlType -> intoEntity[propName] = this.getTimestamp(column.name)?.toInstant()
        LocalDateTimeSqlType -> intoEntity[propName] = this.getTimestamp(column.name)?.toLocalDateTime()
        LocalDateSqlType -> intoEntity[propName] = this.getDate(column.name)?.toLocalDate()
        LocalTimeSqlType -> intoEntity[propName] = this.getTime(column.name)?.toLocalTime()
        TextSqlType -> intoEntity[propName] = this.getString(column.name)
        BlobSqlType -> intoEntity[propName] = this.getBlob(column.name)?.let {
            try {
                it.binaryStream.use { s -> s.readBytes() }
            } finally {
                it.free()
            }
        }

        MonthDaySqlType -> intoEntity[propName] = this.getString(column.name)?.let { MonthDay.parse(it, formatterMonthDay) }
        YearMonthSqlType -> intoEntity[propName] = this.getString(column.name)?.let { YearMonth.parse(it, formatterYearMonth) }
        YearSqlType -> intoEntity[propName] = Year.of(this.getInt(column.name))
        UuidSqlType -> intoEntity[propName] = this.getObject(column.name) as? UUID
    }

}

fun <R> ResultSet.map(block: (ResultSet) -> R): List<R> {
    val list = mutableListOf<R>()
    while (next()) {
        list.add(block(this))
    }
    return list
}

fun <R> ResultSet.useMap(block: (ResultSet) -> R): List<R> {
    val list = mutableListOf<R>()
    this.use { rs ->
        while (rs.next()) {
            list.add(block(rs))
        }
    }
    return list
}

fun ResultSet.getMatchFieldValue(name: String, type: Class<*>): Any? {
    val col = (1..this.metaData.columnCount).find {
        this.metaData.getColumnName(it).conv().lowercase() == name.conv().lowercase()
    } ?: return null
    return when (type) {
        Boolean::class.java -> getBoolean(col)
        Int::class.java -> getInt(col)
        Short::class.java -> getShort(col)
        Long::class.java -> getLong(col)
        Float::class.java -> getFloat(col)
        Double::class.java -> getDouble(col)
        String::class.java -> getString(col)
        BigDecimal::class.java -> getBigDecimal(col)
        ByteArray::class.java -> getBytes(col)
        Timestamp::class.java -> getTimestamp(col)
        Date::class.java -> getDate(col)
        Time::class.java -> getTime(col)
        Instant::class.java -> getTimestamp(col)?.toInstant()
        LocalDateTime::class.java -> getTimestamp(col)?.toLocalDateTime()
        LocalDate::class.java -> getDate(col)?.toLocalDate()
        LocalTime::class.java -> getTime(col)?.toLocalTime()
        MonthDay::class.java -> getString(col)?.let { MonthDay.parse(it, formatterMonthDay) }
        YearMonth::class.java -> getString(col)?.let { YearMonth.parse(it, formatterYearMonth) }
        Year::class.java -> Year.of(getInt(col))
        UUID::class.java -> getObject(col) as? UUID
        else -> null
    }
}

fun <E : Any> ResultSet.setField(entity: E, field: Field) {
    val v = getMatchFieldValue(field.name, field.type)
    when (field.type) {
        Boolean::class.java -> field.setBoolean(entity, v as Boolean)
        Int::class.java -> field.setInt(entity, v as Int)
        Short::class.java -> field.setShort(entity, v as Short)
        Long::class.java -> field.setLong(entity, v as Long)
        Float::class.java -> field.setFloat(entity, v as Float)
        Double::class.java -> field.setDouble(entity, v as Double)
        else -> field.set(entity, v)
    }
}

inline fun <reified E : Any> createEntity(row: ResultSet): E {
    val entity = E::class.java.getDeclaredConstructor().newInstance()
    val fields = E::class.java.declaredFields
    fields.forEach { field ->
        field.isAccessible = true
        row.setField(entity, field)
    }
    return entity
}

inline fun <reified E: Entity<E>, T> Entity<E>.setFieldValue(field: Field, dc: T) {
    // 找不到字段，退出
    val mem = entityClass.members.find { it is KProperty<*> && it.name.conv().lowercase() == field.name.conv().lowercase() && it.returnType.jvmErasure.java == field.type } ?: return
    // 根据不同的类型设置数据
    when(field.type) {
        Boolean::class.java -> this[mem.name] = field.getBoolean(dc)
        Int::class.java -> this[mem.name] = field.getInt(dc)
        Short::class.java -> this[mem.name] = field.getShort(dc)
        Long::class.java -> this[mem.name] = field.getLong(dc)
        Float::class.java -> this[mem.name] = field.getFloat(dc)
        Double::class.java -> this[mem.name] = field.getDouble(dc)
        else -> this[mem.name] = field.get(dc)
    }
}

inline fun <reified E: Entity<E>> instanceEntity(): E {
    val fCompanion = E::class.java.getDeclaredField("Companion")
    val objCompanion = fCompanion.get(E::class.java)
    val mInv = objCompanion::class.java.superclass.getDeclaredMethod("invoke")
    return mInv.invoke(objCompanion) as E
}

inline fun <reified E : Entity<E>> dataClassToEntity(dc: Any): Entity<E> {
    val fields = dc::class.java.declaredFields
    val entity = instanceEntity<E>()
    fields.forEach { field ->
        field.isAccessible = true
        entity.setFieldValue(field, dc)
    }
    return entity
}

inline fun <reified T: Any> entityToDataClass(entity: Entity<*>): T {
    val t = T::class.java.getDeclaredConstructor().newInstance()
    entityFillIntoDataClass(entity, t)
    return t
}

fun entityFillIntoDataClass(entity: Entity<*>, dc: Any) {
    dc::class.java.declaredFields.forEach { field ->
        field.isAccessible = true
        if (entity.properties.containsKey(field.name)) {
            field.set(dc, entity.properties[field.name])
        }
    }
}

fun String.conv(upperFirst: Boolean = false): String {
    val tmp = split("_").joinToString(separator = "") { it.toTitleUpperCase() }
    return if (upperFirst) tmp else tmp[0].lowercase() + tmp.drop(1)
}