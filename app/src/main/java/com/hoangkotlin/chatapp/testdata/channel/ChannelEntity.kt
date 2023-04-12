package com.hoangkotlin.chatapp.testdata.channel

import androidx.room.*
import com.hoangkotlin.chatapp.testdata.message.ChatMessage
import com.hoangkotlin.chatapp.testdata.user.User

@Entity(
    tableName = CHANNEL_ENTITY_TABLE_NAME_TEST,
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["createdByUserId"]
        ),
        ForeignKey(
            entity = ChatMessage::class,
            parentColumns = ["id"],
            childColumns = ["lastMessageId"]
        )
    ],
    indices = [Index("createdByUserId"),Index("lastMessageId")]
)
data class ChatChannel(
    @PrimaryKey val cid: String = "",
    val type: String = "",
    val name: String = "",
    val image: String = "",
    val createdByUserId: String = "",
    val lastMessageId: String? = null,
    val lastMessageAt: Long? = null,
    val createdAt: Long,
    val updatedAt: Long? = null,
    val deletedAt: Long? = null
) {
    constructor() : this(createdAt = 0)
}

const val CHANNEL_ENTITY_TABLE_NAME_TEST = "chat_channel_table"