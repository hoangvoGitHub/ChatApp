package com.hoangkotlin.chatapp.testdata.channel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatChannelDao {
    @Query("SELECT * FROM $CHANNEL_ENTITY_TABLE_NAME_TEST")
    fun getAll(): Flow<List<ChatChannel>>

    @Query("SELECT * FROM $CHANNEL_ENTITY_TABLE_NAME_TEST WHERE cid =:cid")
    fun getChannelById(cid: String): ChatChannel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(channel: ChatChannel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(channels: List<ChatChannel>)

    @Query("DELETE FROM $CHANNEL_ENTITY_TABLE_NAME_TEST")
    fun deleteAll()

}