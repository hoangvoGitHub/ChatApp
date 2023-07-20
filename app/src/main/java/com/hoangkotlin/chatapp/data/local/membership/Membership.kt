package com.hoangkotlin.chatapp.data.local.membership

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hoangkotlin.chatapp.data.local.user.User
import com.hoangkotlin.chatapp.data.local.channel.ChatChannel

@Entity(
    tableName = MEMBERSHIP_ENTITY_TABLE_NAME,
    foreignKeys = [
        ForeignKey(entity = ChatChannel::class, parentColumns = ["cid"], childColumns = ["cid"]),
        ForeignKey(entity = User::class, parentColumns = ["uid"], childColumns = ["uid"])
    ],
    indices = [Index("uid"), Index("cid")]
)
class Membership(
    @PrimaryKey val id: String,
    val cid: String,
    val uid: String,
    val role: String = "",
    val createdAt: Long,
    val updatedAt: Long? = null,
    val deletedAt: Long? = null
) {
    constructor() : this("", "", "", "", 0, 0, 0)
}

const val MEMBERSHIP_ENTITY_TABLE_NAME = "chat_member_ship_table"