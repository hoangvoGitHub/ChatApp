package com.hoangkotlin.chatapp.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query(value = "SELECT * FROM $USER_ENTITY_TABLE_NAME WHERE uid NOT LIKE :currentUserId")
    fun getAllUsers(currentUserId:String): Flow<List<User>>

    @Query("SELECT * FROM $USER_ENTITY_TABLE_NAME WHERE uid =:userUid")
    fun getByUid(userUid: String): User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllUsers(users: List<User>)

    @Query("DELETE FROM $USER_ENTITY_TABLE_NAME")
    suspend fun deleteAllUsers()
}