package com.hoangkotlin.chatapp.logindata

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.hoangkotlin.chatapp.logindata.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource, private val context: Context) {


    private val logInReference: SharedPreferences = context.getSharedPreferences("logged_in_user", Context.MODE_PRIVATE)
    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // Initialize the logged in user from SharedPreferences

        val username = logInReference.getString("username", null)
        val password = logInReference.getString("password", null)
        val displayName = logInReference.getString("displayName", null)
        Log.d("Login Repository", "Test${username} and $password $displayName")
        user = if (username != null && password != null ) {
            Log.d("Login Repository", "Test Set user")
            LoggedInUser(username, password, "displayName!!")
        } else {
            null
        }
    }

    fun logout() {
        user = null
        logInReference.edit().clear().apply()
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
            Log.d("Login Repository", "v${result.data.username} and ${result.data.displayName}")
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}