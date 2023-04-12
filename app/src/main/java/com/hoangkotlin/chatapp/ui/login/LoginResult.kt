package com.hoangkotlin.chatapp.ui.login

import com.hoangkotlin.chatapp.ui.login.LoggedInUserView

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)