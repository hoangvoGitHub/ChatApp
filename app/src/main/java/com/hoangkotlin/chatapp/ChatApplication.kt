package com.hoangkotlin.chatapp

import android.app.Application
import com.hoangkotlin.chatapp.data.local.database.AppDatabase

class ChatApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}