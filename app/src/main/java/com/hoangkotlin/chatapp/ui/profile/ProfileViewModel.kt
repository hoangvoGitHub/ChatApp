package com.hoangkotlin.chatapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoangkotlin.chatapp.ChatApplication
import com.hoangkotlin.chatapp.testdata.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val application: ChatApplication) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getUser(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(application.database.chatUserDao().getByUid(uid))
        }
    }
}