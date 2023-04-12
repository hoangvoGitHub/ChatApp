package com.hoangkotlin.chatapp.testdata.message

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    @Query("SELECT * FROM $MESSAGE_ENTITY_TABLE_NAME_TEST")
    fun getAll(): Flow<List<ChatMessage>>

    @Query("SELECT * FROM $MESSAGE_ENTITY_TABLE_NAME_TEST WHERE id = :messageId")
    fun getMessageById(messageId: String): ChatMessage

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: ChatMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<ChatMessage>)

    @Query("DELETE FROM $MESSAGE_ENTITY_TABLE_NAME_TEST")
    fun deleteAll()

}