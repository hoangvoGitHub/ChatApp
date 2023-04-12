package com.hoangkotlin.chatapp.testdata.channel

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hoangkotlin.chatapp.data.model.User

@Entity(tableName = CHANNEL_ENTITY_TABLE_NAME_TEST)
data class ChatChannel(
    @PrimaryKey val cid: String = "",
    val type: String = "",
    val name: String = "",
    val image: String = "",
    val createdByUserId: String = "",
    val lastMessageId: String = "",
    val lastMessageAt: Long = 0L,
    @Embedded
    val members: List<User>,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val deletedAt: Long = 0L
)

const val CHANNEL_ENTITY_TABLE_NAME_TEST = "chat_channel_table"