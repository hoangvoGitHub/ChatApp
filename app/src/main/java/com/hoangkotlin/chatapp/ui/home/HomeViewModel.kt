package com.hoangkotlin.chatapp.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.hoangkotlin.chatapp.ChatApplication
import com.hoangkotlin.chatapp.repository.MessageRepository
import com.hoangkotlin.chatapp.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.IOException
import com.hoangkotlin.chatapp.logindata.Result
import com.hoangkotlin.chatapp.data.local.channel.ChatChannel
import com.hoangkotlin.chatapp.data.local.message.ChatMessage
import com.hoangkotlin.chatapp.data.local.user.User
import com.hoangkotlin.chatapp.utils.SyncStatus
import kotlinx.coroutines.flow.flowOf

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
        retrieveCurrentUser()
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        Log.d("refreshDataFromRepository", "Being called")

        viewModelScope.launch {
            try {
                userRepository.refreshUsers()
                messageRepository.refreshData()
                Log.d("refreshDataFromRepository", "Done")

            } catch (networkError: IOException) {
                Log.d("Home ViewModel Error", networkError.message!!)
            }
        }
    }


    fun allUser(): Flow<List<User>> =
        userRepository.retrieveUsersFromLocal(_currentDomainUser!!.uid)

    fun currentMessageHistory(): Flow<List<ChatMessage>> {
        if (_currentChannel.value == null) {
            Log.d("Call in MessageHistory", "CurrentChannel is null")
            return flowOf(emptyList())
        }
        Log.d("Call in MessageHistory", "CurrentChannel is &${_currentChannel.value!!.cid}")
        return messageRepository.retrieveMessagesFromLocal(_currentChannel.value!!.cid)
//        return  messageRepository.retrieveAllMessage()
    }


    fun updateChatFriend(friend: User) {
        _currentFriend.value = friend
        updateCurrentChannel()
    }

    private fun updateCurrentChannel() {
        val userIds = listOf(currentDomainUser!!.uid, _currentFriend.value!!.uid)
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.retrieveChannel(userIds) { it ->
                if (it is Result.Success) {
                    _currentChannel.postValue(it.data)
                    Log.d("Call in updateCurrentChannel", "CurrentChannel is &${it.data.cid}")
                } else {
                    // TODO: Display error
                }
            }

        }
    }

    private fun retrieveCurrentUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
//            _currentDomainUser =
        } else {
            _currentDomainUser = User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName!!,
                email = firebaseUser.email!!
            )
        }

    }

    fun setCurrentUser(user: User){
        _currentDomainUser = user
    }

    fun sendMessage(message: String) {
        val random = Random(System.currentTimeMillis()).nextInt().toString()
        val newMessage = ChatMessage(
            id = random,
            cid = _currentChannel.value!!.cid,
            uid = _currentDomainUser!!.uid,
            content = message,
            replyToMessage = null,
            createdLocallyAt = System.currentTimeMillis(),
            syncStatus = SyncStatus.IN_PROGRESS
        )
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.sendMessage(newMessage)
        }


    }

}