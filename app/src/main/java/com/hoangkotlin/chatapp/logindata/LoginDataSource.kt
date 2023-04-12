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

    @SuppressLint("LogNotTimber")
    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val firebaseResult = auth.signInWithEmailAndPassword(username, password).await()

            val user = LoggedInUser(
                firebaseResult.user!!.uid,
                password,
                firebaseResult.user!!.displayName ?: ""
            )
            Log.d("Loggin User", "${user.username} and ${user.displayName}")
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