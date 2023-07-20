package com.hoangkotlin.chatapp.repository

import android.util.Log
import com.hoangkotlin.chatapp.firebase.FirebaseService
import com.hoangkotlin.chatapp.data.local.database.AppDatabase
import com.hoangkotlin.chatapp.data.local.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(
    private val database: AppDatabase,
    private val lifecycleScope: CoroutineScope
) {

    /**
     * Retrieve user from real-time database and insert to the local database
     */
    suspend fun refreshUsers() {
        withContext(Dispatchers.IO) {
            FirebaseService.fetchUserList(database, lifecycleScope){
                Log.d("GetList In Insert all", "List size ${it.size}")
                database.chatUserDao().insertAllUsers(it)
            }
        }
    }


    fun retrieveUsersFromLocal(cuid:String): Flow<List<User>> {
        return database.chatUserDao().getAllUsers(cuid)
    }


//    fun getUserList() = networkBoundResource(
//        query = {
//            Log.d("query", "Being called")
//            database.userDao().getAllUsers()
//        },
//        fetch = {
//            Log.d("fetch", "Being called Done ${FirebaseService.getUserList().size}")
//            FirebaseService.getUserList()
//
//        },
//        saveFetchResult = { userList ->
//            database.withTransaction {
//                database.userDao().deleteAllUsers()
//                database.userDao().insertAllUsers(userList)
//
//                Log.d("saveFetchResult With transaction", "Being called ${userList.size}")
//
//            }
//
//        }
//    )
}