package com.hoangkotlin.chatapp.logindata

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.hoangkotlin.chatapp.logindata.model.LoggedInUser
import kotlinx.coroutines.tasks.await
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val auth = FirebaseAuth.getInstance()

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val firebaseResult = auth.signInWithEmailAndPassword(username, password).await()

            val user = LoggedInUser(
                username,
                password,
                firebaseResult.user!!.displayName!!,
                firebaseResult.user!!.uid
            )
            return Result.Success(user)

        } catch (e: FirebaseAuthInvalidUserException) {
            return Result.Error(IOException("Invalid email address", e))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            return Result.Error(IOException("Invalid password", e))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }

    }

    fun logout() {
        auth.signOut()
    }
}