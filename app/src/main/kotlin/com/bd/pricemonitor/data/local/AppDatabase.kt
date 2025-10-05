package com.bd.pricemonitor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bd.pricemonitor.data.local.dao.CategoryDao
import com.bd.pricemonitor.data.local.dao.ProductDao
import com.bd.pricemonitor.data.local.dao.SubCategoryDao
import com.bd.pricemonitor.data.local.entity.CategoryEntity
import com.bd.pricemonitor.data.local.entity.ProductEntity
import com.bd.pricemonitor.data.local.entity.SubCategoryEntity

@Database(
    entities = [CategoryEntity::class, SubCategoryEntity::class, ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun subCategoryDao(): SubCategoryDao
    abstract fun productDao(): ProductDao
}
