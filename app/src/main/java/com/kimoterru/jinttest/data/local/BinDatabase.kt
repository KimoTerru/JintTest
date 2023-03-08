package com.kimoterru.jinttest.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kimoterru.jinttest.data.local.model.BinEntity

@Database(entities = [BinEntity::class], version = 1)
abstract class BinDatabase: RoomDatabase(){
    abstract fun provideBinDao(): BinDao

    object DataBaseHelper {
        fun getDataBase(context: Context): BinDatabase = Room.databaseBuilder(
            context, BinDatabase::class.java,
            "bin_db.db"
        ).build()
    }
}