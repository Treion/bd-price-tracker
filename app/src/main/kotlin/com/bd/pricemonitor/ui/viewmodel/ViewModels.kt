package com.bd.pricemonitor.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewModelScope
import com.bd.pricemonitor.data.Repository
import com.bd.pricemonitor.data.local.entity.CategoryEntity
import com.bd.pricemonitor.data.local.entity.ProductEntity
import com.bd.pricemonitor.data.local.entity.SubCategoryEntity
import com.bd.pricemonitor.settings.SettingsStore
import com.bd.pricemonitor.sync.SyncWorker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class AppViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = Repository(app)
    private val settings = SettingsStore(app)

    val darkMode = settings.darkMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val syncHours = settings.syncHours.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 24)

    val categories: StateFlow<List<CategoryEntity>> = repository.observeCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun subcategories(categoryId: Long): StateFlow<List<SubCategoryEntity>> = repository.observeSubcategories(categoryId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun products(subcategoryId: Long): StateFlow<List<ProductEntity>> = repository.observeProducts(subcategoryId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun product(productId: Long): StateFlow<ProductEntity?> = repository.observeProduct(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun search(query: String): Flow<List<ProductEntity>> = repository.searchProductsFlow(query)

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { settings.setDarkMode(enabled) }
    }

    fun setSyncHours(hours: Int) {
        viewModelScope.launch {
            settings.setSyncHours(hours)
            SyncWorker.schedule(getApplication())
        }
    }
}
