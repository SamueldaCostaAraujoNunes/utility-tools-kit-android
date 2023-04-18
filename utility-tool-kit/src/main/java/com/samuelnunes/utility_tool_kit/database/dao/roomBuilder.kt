package com.samuelnunes.utility_tool_kit.database.dao

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

inline fun <T: RoomDatabase> buildRoomDatabase(context: Context, klass: Class<T>, name: String, builderAction: RoomDatabase.Builder<T>.() -> Unit = {}): T =
    Room.databaseBuilder(context, klass, name).apply(builderAction).build()