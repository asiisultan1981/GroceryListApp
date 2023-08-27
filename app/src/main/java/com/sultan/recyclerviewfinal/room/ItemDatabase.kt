package com.sultan.recyclerviewfinal.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sultan.recyclerviewfinal.Item

@Database(entities = [Item::class], version = 1)
abstract class ItemDatabase: RoomDatabase() {
    abstract val itemDao: ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemDatabase ?= null

        fun getInstance(context: Context): ItemDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ItemDatabase::class.java,
                        "item_db"
                    ).build()
                }
                return instance
            }
        }
    }

}