package com.hoangkotlin.chatapp.repository

import com.hoangkotlin.chatapp.firebase.FirebaseService
import com.hoangkotlin.chatapp.testdata.channel.ChatChannel
import com.hoangkotlin.chatapp.testdata.database.TestAppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.hoangkotlin.chatapp.logindata.Result

class MessageRepository(
    private val database: TestAppDatabase,
    private val lifecycleScope: CoroutineScope
) {
    suspend fun retrieveChannel(userIds: List<String>, callback: (Result<ChatChannel>) -> Unit) {
        withContext(Dispatchers.IO) {
            val channel = database.chatChannelDao().getChannelByUsers(userIds)
            if (channel == null) {
                FirebaseService.createNewChannel(userIds) {
                    callback(it)
                }
            } else {
                callback(Result.Success(channel))
            }
        }
    }

}