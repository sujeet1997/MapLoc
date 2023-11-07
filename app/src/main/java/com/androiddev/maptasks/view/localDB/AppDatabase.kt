package com.androiddev.maptasks.view.localDB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddev.maptasks.utils.Converters
import com.androiddev.maptasks.view.localDB.tables.CustomerListData

@Database(version = 4, entities = [CustomerListData::class])
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao: RoomApi?
}