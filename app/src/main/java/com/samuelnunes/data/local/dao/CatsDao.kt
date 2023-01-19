package com.samuelnunes.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.samuelnunes.data.local.entitys.BreedWithImage
import kotlinx.coroutines.flow.Flow

@Dao
interface CatsDao {

    @Transaction
    @Query("SELECT * FROM BreedEntity WHERE breedentity.breedId=:breedId LIMIT 1")
    fun getBreed(breedId: String): Flow<BreedWithImage>

    fun getAll(isAsc: Boolean) = if (isAsc) getAllAsc() else getAllDesc()

    @Transaction
    @Query("SELECT * FROM BreedEntity ORDER BY breedentity.name ASC")
    fun getAllAsc(): Flow<List<BreedWithImage>>

    @Transaction
    @Query("SELECT * FROM BreedEntity ORDER BY breedentity.name DESC")
    fun getAllDesc(): Flow<List<BreedWithImage>>

}
