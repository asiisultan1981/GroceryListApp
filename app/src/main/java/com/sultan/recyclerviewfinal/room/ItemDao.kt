package com.sultan.recyclerviewfinal.room

import androidx.room.*
import com.sultan.recyclerviewfinal.Item

@Dao
interface ItemDao {

    @Insert
    suspend fun insertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM item_table ORDER BY id DESC")
    suspend fun showALlItems(): List<Item>
}