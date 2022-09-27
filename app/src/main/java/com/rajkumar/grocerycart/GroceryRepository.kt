package com.rajkumar.grocerycart

import com.rajkumar.grocerycart.GroceryDatabase
import com.rajkumar.grocerycart.GroceryItems


class GroceryRepository(private val db: GroceryDatabase) {
    suspend fun insert(items: GroceryItems) = db.getGroceryDao().insert(items)
    suspend fun delete(items: GroceryItems) = db.getGroceryDao().delete(items)

    fun getAllItems() = db.getGroceryDao().getAllGroceryItems()
}