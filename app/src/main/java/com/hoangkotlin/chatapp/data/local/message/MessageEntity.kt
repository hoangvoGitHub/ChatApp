package com.hoangkotlin.chatapp.data.local.message

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hoangkotlin.chatapp.data.local.user.User
import com.hoangkotlin.chatapp.data.local.channel.ChatChannel
import com.hoangkotlin.chatapp.utils.SyncStatus

@Entity(
    tableName = MESSAGE_ENTITY_TABLE_NAME_TEST,
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["uid"],
        childColumns = ["uid"],
    ),
    ForeignKey(entity = ChatChannel::class,
        parentColumns = ["cid"],
        childColumns = ["cid"]
        )],
    indices = [Index("uid"), Index("cid")]

)
data class ChatMessage(
    @PrimaryKey val id: String,
    val cid: String,
    val uid: String,
    val content: String,
    val replyToMessage: String?,
    val type: String = "message",
    val replyCount: Int = 0,
    val createdAt: Long = 0,
    val createdLocallyAt: Long = 0,
    val updatedAt: Long = 0,
    val updatedLocallyAt: Long = 0,
    val deletedAt: Long = 0,
    val syncStatus: SyncStatus = SyncStatus.IN_PROGRESS,
) {
    constructor() : this("", "", "", "", "")
}

const val MESSAGE_ENTITY_TABLE_NAME_TEST = "chat_message_table"