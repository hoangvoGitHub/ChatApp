package com.hoangkotlin.chatapp.repository

import com.hoangkotlin.chatapp.data.database.AppDatabase
import kotlinx.coroutines.CoroutineScope

class MessageRepository(
    private val database: AppDatabase,
    private val lifecycleScope: CoroutineScope
) {

}