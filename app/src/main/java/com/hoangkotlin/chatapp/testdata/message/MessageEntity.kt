package com.hoangkotlin.chatapp.testdata.message

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hoangkotlin.chatapp.data.model.User
import com.hoangkotlin.chatapp.utils.SyncStatus

@Entity(
    tableName = MESSAGE_ENTITY_TABLE_NAME_TEST
)
data class ChatMessage(
    @PrimaryKey val id: String,
    val cid: String,
    @Embedded
    val sender: User,
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

const val MESSAGE_ENTITY_TABLE_NAME_TEST = "chat_message_table"