package com.hoangkotlin.chatapp.data.model

import androidx.room.*
import com.hoangkotlin.chatapp.utils.SyncStatus
import com.squareup.moshi.JsonClass

@Entity(tableName = USER_ENTITY_TABLE_NAME)
data class User(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val status: String = "",
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
) {
}


@Entity(tableName = CHANNEL_ENTITY_TABLE_NAME)
data class Channel(
    @PrimaryKey val cid: String = "",
    val type: String = "",
    val name: String = "",
    val image: String = "",
    val createdByUserId: String = "",
    val lastMessageId: String = "",
    val lastMessageAt: Long = 0L,
    val members: Map<String,MemberEntity>,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val deletedAt: Long = 0L
) {
  }


@Entity(
    tableName = MESSAGE_ENTITY_TABLE_NAME
)
data class ChatMessage(
    @PrimaryKey val id: String,
    val cid: String,
    @Embedded
//    val sender: MemberEntity,
    val content: String,
    val replyToMessage: String?,
    val type: String = "message",
    val replyCount: Int = 0,
    val createdAt: Long = 0,
    val createdLocallyAt: Long = 0,
    val updatedAt: Long = 0,
    val updatedLocallyAt: Long = 0,
    val deletedAt: Long = 0,
    val syncStatus: SyncStatus = SyncStatus.COMPLETED,
) {
  }


const val CHANNEL_ENTITY_TABLE_NAME = "chat_channel"
const val USER_ENTITY_TABLE_NAME = "chat_user"
const val MESSAGE_ENTITY_TABLE_NAME = "chat_message"







