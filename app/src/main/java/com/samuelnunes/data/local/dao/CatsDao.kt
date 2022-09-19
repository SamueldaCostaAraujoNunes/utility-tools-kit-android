package com.samuelnunes.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.samuelnunes.data.dto.response.BreedDTO
import com.samuelnunes.utility_tool_kit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CatsDao : BaseDao<BreedDTO> {


    @Query("SELECT * FROM BreedDTO WHERE id=:id")
    fun getBreed(id: String): Flow<BreedDTO>

    fun getAll(isAsc: Boolean) = if (isAsc) getAllAsc() else getAllDesc()

    @Query("SELECT * FROM BreedDTO ORDER BY name ASC")
    fun getAllAsc(): Flow<List<BreedDTO>>

    @Query("SELECT * FROM BreedDTO ORDER BY name DESC")
    fun getAllDesc(): Flow<List<BreedDTO>>

}
