package com.hoangkotlin.chatapp.testdata.user

import androidx.room.*

@Entity(tableName = USER_ENTITY_TABLE_NAME)
data class User(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val image: String? =null,
    val status: String = "",
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
) {
    constructor() : this("", "", "")
}







const val USER_ENTITY_TABLE_NAME = "chat_user"







