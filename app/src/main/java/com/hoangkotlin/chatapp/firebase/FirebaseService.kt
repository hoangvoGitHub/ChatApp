package com.hoangkotlin.chatapp.firebase

import android.annotation.SuppressLint
import android.content.ContentValues

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hoangkotlin.chatapp.data.domain.DomainMessage
import com.hoangkotlin.chatapp.data.model.*
import com.hoangkotlin.chatapp.firebase.utils.RealtimeReference
import com.hoangkotlin.chatapp.data.local.channel.ChatChannel
import com.hoangkotlin.chatapp.data.local.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.hoangkotlin.chatapp.logindata.Result

import com.hoangkotlin.chatapp.data.local.membership.Membership
import com.hoangkotlin.chatapp.data.local.message.ChatMessage
import com.hoangkotlin.chatapp.data.local.user.User
import com.hoangkotlin.chatapp.utils.SyncStatus
import com.hoangkotlin.chatapp.utils.asDomainMessage
import java.io.IOException


class FirebaseService {

    companion object {
        private val database = Firebase.database
        fun fetchUserList(
            localDatabase: AppDatabase,
            lifecycleScope: CoroutineScope,
            callback: suspend (List<User>) -> Unit
        ) {
            database.getReference(RealtimeReference.USER)
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

        fun fetchChannel(
            lifecycleScope: CoroutineScope, callback: suspend (List<ChatChannel>) -> Unit
        ) {
            database.getReference(RealtimeReference.CHANNEL)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val dataList = dataSnapshot.children.mapNotNull { snapshot ->
                            snapshot.getValue(ChatChannel::class.java)
                        }
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                Log.d("GetList", "List size ${dataList.size}")
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


        fun sendMessage(
            message: ChatMessage,
            callback: (SyncStatus) -> Unit
        ) {
            val domainMessage = message.asDomainMessage()
            database.getReference(RealtimeReference.MESSAGE).push().setValue(domainMessage)
                .addOnSuccessListener {
                    callback(SyncStatus.COMPLETED)
                }.addOnFailureListener {
                callback(SyncStatus.FAILED_PERMANENTLY)
            }.addOnCanceledListener {
                callback(SyncStatus.AWAITING_ATTACHMENTS)
            }

        }

        fun createNewChannel(
            userIds: List<String>, channelCallback: (Result<ChatChannel>) -> Unit
        ) {
            val ref = database.reference
            val createAt = System.currentTimeMillis()
            val cidKey = database.getReference(RealtimeReference.CHANNEL).push().key
            val newChannel = ChatChannel(
                cid = cidKey!!,
                type = "messaging",
                name = "",
                createdByUserId = userIds[0],
                createdAt = createAt
            )

            val childUpdate = HashMap<String, Any>()
            childUpdate["${RealtimeReference.CHANNEL}/$cidKey"] = newChannel
            userIds.forEachIndexed { index, uid ->
                val key = database.getReference(RealtimeReference.MEMBERSHIP).push().key
                val role = "host".takeIf { index == 0 } ?: "member"
                val newMembership = Membership(
                    id = key!!, cid = cidKey, uid = uid, createdAt = createAt, role = role
                )

                childUpdate["${RealtimeReference.MEMBERSHIP}/$key"] = newMembership
            }
            ref.updateChildren(childUpdate).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    channelCallback(Result.Success(newChannel))
                } else {
                    channelCallback(
                        Result.Error(
                            IOException(
                                "Error create channel", task.exception
                            )
                        )
                    )
                }
            }

        }

        fun fetchMembership(lifecycleScope: CoroutineScope, callback: (List<Membership>) -> Unit) {
            database.getReference(RealtimeReference.MEMBERSHIP)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val dataList = dataSnapshot.children.mapNotNull { snapshot ->
                            snapshot.getValue(Membership::class.java)
                        }
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                Log.d("GetList", "List size ${dataList.size}")
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

        fun fetchMessage(lifecycleScope: CoroutineScope, callback: (List<DomainMessage>) -> Unit) {
            database.getReference(RealtimeReference.MESSAGE)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val dataList = dataSnapshot.children.mapNotNull { snapshot ->
                            snapshot.getValue(DomainMessage::class.java)
                        }
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                Log.d("GetList", "List size ${dataList.size}")
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


    }
}


