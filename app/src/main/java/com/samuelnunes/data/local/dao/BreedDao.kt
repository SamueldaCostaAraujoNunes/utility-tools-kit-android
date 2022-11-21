package com.samuelnunes.data.local.dao

import androidx.room.Dao
import com.samuelnunes.data.local.entitys.BreedEntity
import com.samuelnunes.utility_tool_kit.database.dao.BaseDao

@Dao
interface BreedDao : BaseDao<BreedEntity>