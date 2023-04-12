package com.hoangkotlin.chatapp.logindata.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val username: String,
    val password: String,
    val displayName: String
) {

}