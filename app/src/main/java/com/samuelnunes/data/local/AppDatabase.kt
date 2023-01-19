package com.samuelnunes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samuelnunes.data.local.dao.BreedDao
import com.samuelnunes.data.local.dao.CatsDao
import com.samuelnunes.data.local.dao.ImageDao
import com.samuelnunes.data.local.entitys.BreedEntity
import com.samuelnunes.data.local.entitys.ImageEntity

@Database(entities = [BreedEntity::class, ImageEntity::class], version = 9, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catsDao(): CatsDao
    abstract fun breedDao(): BreedDao
    abstract fun imageDao(): ImageDao
}