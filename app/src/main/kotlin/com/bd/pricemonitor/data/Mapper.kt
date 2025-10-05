package com.bd.pricemonitor.data

import com.bd.pricemonitor.data.local.entity.CategoryEntity
import com.bd.pricemonitor.data.local.entity.SubCategoryEntity
import com.bd.pricemonitor.data.local.entity.ProductEntity
import com.bd.pricemonitor.data.remote.ApiCategory
import com.bd.pricemonitor.data.remote.ApiSubCategory
import com.bd.pricemonitor.data.remote.ApiProduct

fun ApiCategory.toEntity() = CategoryEntity(id = id, name = name, icon = icon)
fun ApiSubCategory.toEntity() = SubCategoryEntity(id = id, categoryId = categoryId, name = name)
fun ApiProduct.toEntity() = ProductEntity(
    id = id,
    subcategoryId = subcategoryId,
    name = name,
    price = price,
    unit = unit,
    sourceUrl = sourceUrl,
    govBody = govBody,
    lastUpdated = lastUpdated
)
