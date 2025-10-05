package com.bd.pricemonitor.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = SubCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["subcategoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("subcategoryId"), Index("name")]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subcategoryId: Long,
    val name: String,
    val price: Double,
    val unit: String,
    val sourceUrl: String,
    val govBody: String,
    val lastUpdated: String
)
