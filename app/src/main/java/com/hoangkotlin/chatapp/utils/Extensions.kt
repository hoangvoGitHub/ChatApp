package com.hoangkotlin.chatapp.utils

import com.hoangkotlin.chatapp.data.domain.DomainMessage
import com.hoangkotlin.chatapp.data.model.ChatMessage
import com.hoangkotlin.chatapp.data.model.MemberEntity
import com.hoangkotlin.chatapp.data.model.User

fun ChatMessage.asDomainMessage(): DomainMessage {
    return DomainMessage(
        id = this.id,
        cid = this.cid,
        sender = this.uid,
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

fun User.asMemberEntity(createAt: Long): MemberEntity {
    return MemberEntity(
        uid = this.uid,
        role = "member",
        memberCreatedAt = createAt,
        memberUpdatedAt = null,
        lastRead = null,
        unreadMessages = 0,
        lastMessageSeenDate = null

    )
}


