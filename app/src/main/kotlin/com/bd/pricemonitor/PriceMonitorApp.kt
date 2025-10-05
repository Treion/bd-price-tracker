package com.bd.pricemonitor

import android.app.Application
import com.bd.pricemonitor.data.Repository
import com.bd.pricemonitor.sync.SyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PriceMonitorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            Repository(this@PriceMonitorApp).seedFromAssetsIfEmpty()
        }
        SyncWorker.schedule(this)
    }
}
