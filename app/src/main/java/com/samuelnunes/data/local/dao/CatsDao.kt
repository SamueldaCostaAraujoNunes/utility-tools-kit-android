package com.samuelnunes.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.samuelnunes.data.dto.response.BreedDTO
import com.samuelnunes.utility_tool_kit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CatsDao: BaseDao<BreedDTO> {

    @Query("SELECT * FROM BreedDTO")
    fun getAll(): Flow<List<BreedDTO>>

}
