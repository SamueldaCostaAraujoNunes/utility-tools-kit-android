package com.samuelnunes.utility_tool_kit.database.dao


import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(value: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(values: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllIgnore(values: List<T>): List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(value: T)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateAll(value: List<T>)

    @Delete
    suspend fun delete(values: List<T>)

    @Delete
    suspend fun delete(value: T)

}

@Transaction
suspend fun <T> BaseDao<T>.insertOrUpdate(values: List<T>) {
    val result = insertAllIgnore(values)
    val update = mutableListOf<T>()
    result.forEachIndexed { index, value ->
        if (value == -1L) update += values[index]
    }
    if (update.isNotEmpty()) updateAll(update)
}

@Transaction
suspend fun <T> BaseDao<T>.insertOrUpdate(value: T) {
    val result = insertIgnore(value)
    if (result == -1L) update(value)
}