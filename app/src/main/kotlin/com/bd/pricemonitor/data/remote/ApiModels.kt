package com.bd.pricemonitor.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiCategory(
    val id: Long,
    val name: String,
    val icon: String? = null
)

@Serializable
data class ApiSubCategory(
    val id: Long,
    @SerialName("category_id") val categoryId: Long,
    val name: String
)

@Serializable
data class ApiProduct(
    val id: Long,
    @SerialName("subcategory_id") val subcategoryId: Long,
    val name: String,
    val price: Double,
    val unit: String,
    @SerialName("source_url") val sourceUrl: String,
    @SerialName("gov_body") val govBody: String,
    @SerialName("last_updated") val lastUpdated: String
)

@Serializable
data class SnapshotResponse(
    val categories: List<ApiCategory>,
    @SerialName("subcategories") val subCategories: List<ApiSubCategory>,
    val products: List<ApiProduct>,
    val generatedAt: String
)
