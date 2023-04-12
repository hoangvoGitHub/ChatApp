package com.hoangkotlin.chatapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MemberEntity(
    val uid: String,
    val role: String,
    val createdAt: Long,
    val updatedAt: Long? = null,
    val lastRead: Long? = null,
    val unreadMessages: Int = 0,
    val lastMessageSeenDate: Long? = null,
    val deletedAt: Long? = null

)