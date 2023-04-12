package com.hoangkotlin.chatapp.logindata.model

data class Relationship(
    val firstUser: String,
    val secondUser: String,
    val status: Boolean
) {
    constructor() : this("", "", true)

    override fun equals(other: Any?): Boolean {
        other as Relationship
        if (firstUser != other.firstUser && firstUser != other.secondUser)
            return false
        if (secondUser != other.firstUser && secondUser != other.secondUser)
            return false
        return true
    }

    fun isInRelationship(firstUid:String, secondUid: String): Boolean{
        if (firstUser != firstUid && firstUser != secondUid)
            return false
        if (secondUser != firstUid && secondUser != secondUid)
            return false
        return true
    }
}