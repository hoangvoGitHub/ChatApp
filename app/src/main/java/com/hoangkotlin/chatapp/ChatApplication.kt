package com.hoangkotlin.chatapp

import android.app.Application
import com.hoangkotlin.chatapp.data.local.database.TestAppDatabase

class ChatApplication : Application() {
    val database: TestAppDatabase by lazy { TestAppDatabase.getDatabase(this) }
}