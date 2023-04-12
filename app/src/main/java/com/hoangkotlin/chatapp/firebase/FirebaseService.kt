package com.hoangkotlin.chatapp.firebase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hoangkotlin.chatapp.data.model.*
import com.hoangkotlin.chatapp.firebase.utils.Reference
import com.hoangkotlin.chatapp.testdata.channel.ChatChannel
import com.hoangkotlin.chatapp.testdata.database.TestAppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.hoangkotlin.chatapp.logindata.Result
import com.hoangkotlin.chatapp.testdata.membership.Membership
import java.io.IOException


class FirebaseService {

    companion object {
        private val database = Firebase.database
        fun fetchUserList(
            localDatabase: TestAppDatabase,
            lifecycleScope: CoroutineScope,
            callback: suspend (List<User>) -> Unit
        ) {
            database.getReference(Reference.USER)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val dataList = dataSnapshot.children.mapNotNull { snapshot ->
                            snapshot.getValue(User::class.java)
                        }
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                Log.d("GetList", "List size ${dataList.size}")
                                localDatabase.chatUserDao().deleteAllUsers()
                                callback(dataList)
                            }
                        }

                        Log.d("GetList", "List size ${dataList.size}")
                    }

                    @SuppressLint("LogNotTimber")
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w(
                            ContentValues.TAG, "loadPost:onCancelled", databaseError.toException()
                        )
                    }
                })
        }

        fun createChannel(
            userList: List<User>,
            lifecycleScope: CoroutineScope,
            callback: suspend (Channel) -> Unit
        ) {
//            val memberMap = mutableMapOf<String, MemberEntity>()
//            val createAt = System.currentTimeMillis()
//            userList.forEach {
//                memberMap[it.name] = it.asMemberEntity(createAt)
//            }
//            var channelName = ""
//            userList.forEach {
//                channelName += it.name
//                channelName += " "
//            }
//            val key = database.getReference(Reference.CHANNEL).push().key
//            val newChannel = DomainChannel(
//                cid = key!!,
//                members = memberMap,
//                createdAt = createAt,
//                name = channelName
//            )
//            database.getReference("${Reference.CHANNEL}/$key").setValue(newChannel)
//                .addOnCompleteListener {
//                    lifecycleScope.launch {
//                        withContext(Dispatchers.IO) {
//                            callback(newChannel.asLocalChannel())
//                        }
//                    }
//
//                }

        }


        fun sendMessage(
            message: ChatMessage,
            lifecycleScope: CoroutineScope,
            callback: suspend (ChatMessage) -> Unit
        ) {
//            val domainMessage = message.asDomainMessage()
//            database.getReference(Reference.MESSAGE).push().setValue(domainMessage)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val updateMessage = message.copy(syncStatus = SyncStatus.COMPLETED)
//                        lifecycleScope.launch {
//                            withContext(Dispatchers.IO) {
//                                callback(updateMessage)
//                            }
//
//                        }
//                    } else {
//                        val updateMessage = message.copy(syncStatus = SyncStatus.FAILED_PERMANENTLY)
//                        lifecycleScope.launch {
//                            withContext(Dispatchers.IO) {
//                                callback(updateMessage)
//                            }
//                        }
//                    }
//                }
        }

        fun createNewChannel(
            userIds: List<String>, callback: (Result<ChatChannel>) -> Unit
        ) {
            val ref = database.reference
            val createAt = System.currentTimeMillis()
            val cidKey = database.getReference(Reference.CHANNEL).push().key
            val newChannel = ChatChannel(
                cid = cidKey!!,
                type = "messaging",
                name = "",
                createdByUserId = userIds[0],
                createdAt = createAt
            )

            val childUpdate = HashMap<String, Any>()
            childUpdate["${Reference.CHANNEL}/$cidKey"] = newChannel
            userIds.forEachIndexed { index, uid ->
                val key = database.getReference(Reference.MEMBERSHIP).push().key
                val role = "host".takeIf { index == 0 } ?: "member"
                val newMembership =
                    Membership(
                        id = key!!,
                        cid = cidKey,
                        uid = uid,
                        createdAt = createAt,
                        role = role
                    )

                childUpdate["${Reference.MEMBERSHIP}/$key"] = newMembership
            }
            ref.updateChildren(childUpdate).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(Result.Success(newChannel))
                } else {
                    callback(Result.Error(IOException("Error create channel", task.exception)))
                }
            }

        }


    }
}


