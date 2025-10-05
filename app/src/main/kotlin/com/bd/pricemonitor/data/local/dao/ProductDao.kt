package com.bd.pricemonitor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bd.pricemonitor.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE subcategoryId = :subcategoryId ORDER BY name")
    fun observeBySubcategory(subcategoryId: Long): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    fun observeById(productId: Long): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' ORDER BY name LIMIT 50")
    fun searchByName(query: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ProductEntity>)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int
}
