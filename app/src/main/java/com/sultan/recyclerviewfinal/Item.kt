package com.sultan.recyclerviewfinal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item(
    var name: String,
    var type:String,
    var rate: Double,
    var quantity: Double,
    var isSelected: Boolean = false,
    var isWished: Boolean = false
):java.io.Serializable {
    fun getPrice(): Double {
        return rate * quantity

    }
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}