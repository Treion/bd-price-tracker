package com.bd.pricemonitor.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

object SettingsKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val SYNC_HOURS = intPreferencesKey("sync_hours")
}

class SettingsStore(private val context: Context) {
    val darkMode: Flow<Boolean> = context.dataStore.data.map { it[SettingsKeys.DARK_MODE] ?: true }
    val syncHours: Flow<Int> = context.dataStore.data.map { it[SettingsKeys.SYNC_HOURS] ?: 24 }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[SettingsKeys.DARK_MODE] = enabled }
    }

    suspend fun setSyncHours(hours: Int) {
        context.dataStore.edit { it[SettingsKeys.SYNC_HOURS] = hours }
    }
}
