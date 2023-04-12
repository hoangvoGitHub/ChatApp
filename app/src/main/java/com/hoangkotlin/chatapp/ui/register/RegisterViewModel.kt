package com.hoangkotlin.chatapp.ui.register

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hoangkotlin.chatapp.R
import com.hoangkotlin.chatapp.firebase.utils.RealtimeReference
import com.hoangkotlin.chatapp.firebase.utils.StorageReference
import com.hoangkotlin.chatapp.testdata.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegisterViewModel : ViewModel() {

    private val _registerFormState = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerFormState

    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> = _registerResult


    var imageUri: Uri? = null


    fun register(username: String, password: String, name: String) {
        Log.d("Register", "Start")

        viewModelScope.launch(Dispatchers.IO) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _registerResult.value = true
                        val currentUser = task.result.user
                        // Update the display name for the user
                        val profileUpdates =
                            UserProfileChangeRequest.Builder().setDisplayName(name).build()
                        currentUser!!.updateProfile(profileUpdates)
                        addUserToDataBase(currentUser.email!!, currentUser.uid, name)
                        uploadProfileImage(currentUser.uid)

                    } else {
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        _registerResult.value = false
                    }
                }
        }
    }


    private fun addUserToDataBase(username: String, uid: String, name: String) {
        viewModelScope.launch {
            Log.d("add user view model scope launch", "Launch")

            val database = Firebase.database
            val userRef = database.getReference(RealtimeReference.USER)

            val newDomainUser = User(uid, name, username)

            try {
                userRef.child(uid).setValue(newDomainUser)
            } catch (e: java.lang.Exception) {
                Log.d("Write Data to Database", e.message.toString())
                Log.d("Write Data to Database", "Error")
            }
        }
    }

    private fun uploadProfileImage(userUid: String) {
        if (imageUri != null) {
            viewModelScope.launch {
                val storageReference = Firebase.storage;
                val ref = storageReference.reference
                    .child("${StorageReference.PROFILE_IMAGE}/$userUid")
                try {
                    if (imageUri != null) {
                        ref.putFile(imageUri!!)
                    }
                } catch (e: Exception) {
                    Log.d("Write Data to Database", e.message.toString())
                    Log.d("Write Data to Database", "Error")
                }
            }
        }
    }

    fun registerDataChanged(
        username: String,
        password: String,
        confirmPassword: String,
        name: String
    ) {
        if (!isUserNameValid(username)) {
            _registerFormState.value =
                RegisterFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerFormState.value =
                RegisterFormState(passwordError = R.string.invalid_password)
        } else if (password.compareTo(confirmPassword, false) != 0) {
            _registerFormState.value =
                RegisterFormState(confirmPasswordError = R.string.password_not_match)
        } else if (!isNameValid(name)) {
            _registerFormState.value =
                RegisterFormState(confirmPasswordError = R.string.name_error)
        } else {
            _registerFormState.value =
                RegisterFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isNameValid(name: String): Boolean {
        return name.isNotBlank() && name.isNotEmpty()
    }
}
