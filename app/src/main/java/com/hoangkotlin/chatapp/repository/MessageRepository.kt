package com.hoangkotlin.chatapp.repository

import android.util.Log
import com.hoangkotlin.chatapp.firebase.FirebaseService
import com.hoangkotlin.chatapp.data.local.channel.ChatChannel
import com.hoangkotlin.chatapp.data.local.database.TestAppDatabase
import com.hoangkotlin.chatapp.logindata.Result
import com.hoangkotlin.chatapp.data.local.message.ChatMessage
import com.hoangkotlin.chatapp.utils.asMessageEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class MessageRepository(
    private val database: TestAppDatabase,
    private val lifecycleScope: CoroutineScope
) {

    suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            FirebaseService.fetchChannel(lifecycleScope) {
                database.chatChannelDao().insertAll(it)
            }
            FirebaseService.fetchMembership(lifecycleScope) {
                database.membershipDao().insertAll(it)
            }
            FirebaseService.fetchMessage(lifecycleScope) { domainMessages ->
                database.chatMessageDao().insertAll(domainMessages.map { it.asMessageEntity() })
            }

        }
    }

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

    fun retrieveMessagesFromLocal(cid: String): Flow<List<ChatMessage>> {
        Log.d("RetrieveMessagesFromLocal", "Being called")
        return database.chatMessageDao().getMessageByChannel(cid)
    }

    fun retrieveAllMessage():Flow<List<ChatMessage>>{
        return database.chatMessageDao().getAll()
    }


    suspend fun sendMessage(message: ChatMessage) {
        withContext(Dispatchers.IO) {
            database.chatMessageDao().insert(message)
            FirebaseService.sendMessage(message) { syncStatus ->
                lifecycleScope.launch(Dispatchers.IO) {
                    database.chatMessageDao().update(message.copy(syncStatus = syncStatus))
                }

            }

        }
    }

}