package com.bd.pricemonitor.data

import android.content.Context
import androidx.room.Room
import com.bd.pricemonitor.BuildConfig
import com.bd.pricemonitor.data.local.AppDatabase
import com.bd.pricemonitor.data.local.entity.CategoryEntity
import com.bd.pricemonitor.data.local.entity.ProductEntity
import com.bd.pricemonitor.data.local.entity.SubCategoryEntity
import com.bd.pricemonitor.data.remote.PriceApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class Repository(private val context: Context) {
    private val db: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "bd_price_monitor.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    private val api by lazy { PriceApiFactory.create(BuildConfig.BASE_URL) }
    private val json by lazy { Json { ignoreUnknownKeys = true } }

    fun observeCategories(): Flow<List<CategoryEntity>> = db.categoryDao().observeAll()
    fun observeSubcategories(categoryId: Long): Flow<List<SubCategoryEntity>> = db.subCategoryDao().observeByCategory(categoryId)
    fun observeProducts(subcategoryId: Long): Flow<List<ProductEntity>> = db.productDao().observeBySubcategory(subcategoryId)
    fun observeProduct(productId: Long): Flow<ProductEntity?> = db.productDao().observeById(productId)

    fun searchProductsFlow(query: String): Flow<List<ProductEntity>> = db.productDao().searchByName(query)

    suspend fun seedFromAssetsIfEmpty() = withContext(Dispatchers.IO) {
        val count = db.productDao().count()
        if (count > 0) return@withContext
        val asset = context.assets.open("seed.json").bufferedReader().use { it.readText() }
        val snap = json.decodeFromString(com.bd.pricemonitor.data.remote.SnapshotResponse.serializer(), asset)
        db.categoryDao().upsertAll(snap.categories.map { it.toEntity() })
        db.subCategoryDao().upsertAll(snap.subCategories.map { it.toEntity() })
        db.productDao().upsertAll(snap.products.map { it.toEntity() })
    }

    suspend fun sync() = withContext(Dispatchers.IO) {
        val snap = api.getSnapshot()
        db.categoryDao().upsertAll(snap.categories.map { it.toEntity() })
        db.subCategoryDao().upsertAll(snap.subCategories.map { it.toEntity() })
        db.productDao().upsertAll(snap.products.map { it.toEntity() })
    }
}
