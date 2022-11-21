package com.samuelnunes.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.samuelnunes.data.local.entitys.ImageEntity
import com.samuelnunes.utility_tool_kit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow


@Dao
interface ImageDao : BaseDao<ImageEntity> {
    @Query("SELECT * FROM ImageEntity WHERE type = 'GIF' LIMIT 1")
    fun getRandomGifs(): Flow<List<ImageEntity>>
}