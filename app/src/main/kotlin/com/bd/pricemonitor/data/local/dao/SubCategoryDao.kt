package com.bd.pricemonitor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bd.pricemonitor.data.local.entity.SubCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubCategoryDao {
    @Query("SELECT * FROM subcategories WHERE categoryId = :categoryId ORDER BY name")
    fun observeByCategory(categoryId: Long): Flow<List<SubCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<SubCategoryEntity>)
}
