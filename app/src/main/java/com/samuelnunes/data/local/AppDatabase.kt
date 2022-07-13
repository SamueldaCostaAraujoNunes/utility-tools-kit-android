package com.samuelnunes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samuelnunes.data.dto.response.BreedDTO
import com.samuelnunes.data.local.dao.CatsDao

@Database(entities = [BreedDTO::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catsDao(): CatsDao
}