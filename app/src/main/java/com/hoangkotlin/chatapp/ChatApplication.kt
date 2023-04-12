package com.hoangkotlin.chatapp

import android.app.Application
import com.hoangkotlin.chatapp.data.database.AppDatabase
import com.hoangkotlin.chatapp.testdata.database.TestAppDatabase

class ChatApplication : Application() {
    val database: TestAppDatabase by lazy { TestAppDatabase.getDatabase(this) }
}