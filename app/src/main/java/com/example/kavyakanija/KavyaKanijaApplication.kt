package com.example.kavyakanija

import android.app.Application
import com.example.kavyakanija.data.local.KavyaKanijaDatabase
import com.example.kavyakanija.data.repository.PoetryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class KavyaKanijaApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { KavyaKanijaDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { PoetryRepository(database.poetryDao()) }
}
