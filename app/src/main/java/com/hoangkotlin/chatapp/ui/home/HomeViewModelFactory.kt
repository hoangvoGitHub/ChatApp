package com.hoangkotlin.chatapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hoangkotlin.chatapp.ChatApplication
import kotlinx.coroutines.CoroutineScope

class HomeViewModelFactory(private val application: ChatApplication, private val lifecycleScope: CoroutineScope) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application, lifecycleScope) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}