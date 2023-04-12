package com.hoangkotlin.chatapp.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.hoangkotlin.chatapp.ChatApplication
import com.hoangkotlin.chatapp.data.model.Channel
import com.hoangkotlin.chatapp.data.model.User
import com.hoangkotlin.chatapp.repository.MessageRepository
import com.hoangkotlin.chatapp.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.IOException
import com.hoangkotlin.chatapp.logindata.Result
import com.hoangkotlin.chatapp.testdata.channel.ChatChannel

import kotlin.random.Random

class HomeViewModel(application: ChatApplication, lifecycleScope: CoroutineScope) : ViewModel() {
    private val userRepository = UserRepository(application.database, lifecycleScope)
    private val messageRepository = MessageRepository(application.database, lifecycleScope)

    private val _currentChannel = MutableLiveData<ChatChannel>()
    val currentChannel: LiveData<ChatChannel>
        get() = _currentChannel

    private val _currentFriend = MutableLiveData<User>()
    val currentFriend: LiveData<User>
        get() = _currentFriend

    private var _currentDomainUser: User? = null
    val currentDomainUser: User?
        get() = _currentDomainUser


    init {
        getUsers()
        retrieveCurrentUser()
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        Log.d("refreshDataFromRepository", "Being called")

        viewModelScope.launch {
            try {
                userRepository.refreshUsers()
//                messageRepository.apply {
//                    val deferred = async { refreshChannel() }
//                    deferred.await()
//                    refreshMessages()
//                    refreshMembership()
//                }
                Log.d("refreshDataFromRepository", "Done")

            } catch (networkError: IOException) {
                Log.d("Home ViewModel Error", networkError.message!!)
            }
        }
    }

    fun allUser(): Flow<List<User>> =
        userRepository.retrieveUsersFromLocal(_currentDomainUser!!.uid)

    lateinit var userList: Flow<List<User>>
    private fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            userList = userRepository.retrieveUsersFromLocal(_currentDomainUser!!.uid)
        }
    }


    fun updateChatFriend(friend: User) {
        _currentFriend.value = friend
    }

    fun updateCurrentChannel() {
        val userIds = listOf(currentDomainUser!!.uid, _currentFriend.value!!.uid)
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.retrieveChannel(userIds) { it ->
                if (it is Result.Success) {
                    _currentChannel.value = it.data
                }
            }

        }
    }

    private fun retrieveCurrentUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        _currentDomainUser = User(
            uid = firebaseUser.uid,
            name = firebaseUser.displayName!!,
            email = firebaseUser.email!!
        )
    }

    fun sendMessage(message: String) {


    }

}