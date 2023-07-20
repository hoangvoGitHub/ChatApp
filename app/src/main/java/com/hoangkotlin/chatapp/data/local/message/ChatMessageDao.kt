package com.hoangkotlin.chatapp.data.local.message

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    @Query("SELECT * FROM $MESSAGE_ENTITY_TABLE_NAME_TEST")
    fun getAll(): Flow<List<ChatMessage>>

    @Query("SELECT * FROM $MESSAGE_ENTITY_TABLE_NAME_TEST WHERE id = :messageId")
    fun getMessageById(messageId: String): ChatMessage

    @Query("SELECT * FROM $MESSAGE_ENTITY_TABLE_NAME_TEST WHERE cid LIKE :currentCid")
    fun getMessageByChannel(currentCid: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: ChatMessage)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(message: ChatMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<ChatMessage>)

    @Query("DELETE FROM $MESSAGE_ENTITY_TABLE_NAME_TEST")
    fun deleteAll()

}