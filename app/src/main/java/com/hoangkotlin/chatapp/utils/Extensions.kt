package com.hoangkotlin.chatapp.utils

//import com.hoangkotlin.chatapp.test_data.domain.DomainMessage

import com.hoangkotlin.chatapp.data.domain.DomainMessage
import com.hoangkotlin.chatapp.data.model.MemberEntity
import com.hoangkotlin.chatapp.data.local.user.User
import com.hoangkotlin.chatapp.logindata.model.LoggedInUser
import com.hoangkotlin.chatapp.data.local.message.ChatMessage

fun ChatMessage.asDomainMessage(): DomainMessage {
    return DomainMessage(
        id = this.id,
        cid = this.cid,
        uid = this.uid,
        content = this.content,
        type = this.type,
        replyToMessage = this.replyToMessage,
        replyCount = this.replyCount,
        createdAt = this.createdAt,
        createdLocallyAt = this.createdLocallyAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

fun DomainMessage.asMessageEntity(): ChatMessage {
    return ChatMessage(
        id = this.id,
        cid = this.cid,
        uid = this.uid,
        content = this.content,
        type = this.type,
        replyToMessage = this.replyToMessage,
        replyCount = this.replyCount,
        createdAt = this.createdAt,
        createdLocallyAt = this.createdLocallyAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt,
        syncStatus = SyncStatus.COMPLETED
    )
}

fun User.asMemberEntity(createAt: Long): MemberEntity {
    return MemberEntity(
        uid = this.uid,
        role = "member",
        createdAt = createAt,
        updatedAt = null,
        lastRead = null,
        unreadMessages = 0,
        lastMessageSeenDate = null,
        deletedAt = null
    )
}

fun LoggedInUser.asUser(): User {
    return User(
        uid = this.uid,
        name = this.displayName,
        email = this.username
    )
}


