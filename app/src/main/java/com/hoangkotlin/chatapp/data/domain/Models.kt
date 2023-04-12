package com.hoangkotlin.chatapp.data.domain

import com.hoangkotlin.chatapp.utils.SyncStatus

data class DomainMessage(
    val id: String,
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
){
    constructor():this("","","","",null)

}