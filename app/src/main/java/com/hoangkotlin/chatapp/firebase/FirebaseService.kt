package com.hoangkotlin.chatapp.firebase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hoangkotlin.chatapp.data.database.AppDatabase
import com.hoangkotlin.chatapp.data.model.*
import com.hoangkotlin.chatapp.firebase.utils.Reference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FirebaseService {

    companion object {
        private val database = Firebase.database
        fun fetchUserList(
            localDatabase: AppDatabase,
            lifecycleScope: CoroutineScope,
            callback: suspend (List<User>) -> Unit
        ) {
            database.getReference(Reference.USER)
                .addValueEventListener(object : ValueEventListener {
                    @SuppressLint("LogNotTimber")
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val dataList = dataSnapshot.children.mapNotNull { snapshot ->
                            snapshot.getValue(User::class.java)
                        }

                        val newData = dataList.filterNot { data ->
                            localDatabase.userDao().getByUid(data.uid) != null
                        }


                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                Log.d("GetList", "List size ${dataList.size}")
                                localDatabase.userDao().deleteAllUsers()
                                callback(dataList)
                            }
                        }

                        Log.d("GetList", "List size ${dataList.size}")
                    }

                    @SuppressLint("LogNotTimber")
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w(
                            ContentValues.TAG,
                            "loadPost:onCancelled",
                            databaseError.toException()
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



    }
}


