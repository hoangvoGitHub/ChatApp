package com.hoangkotlin.chatapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.hoangkotlin.chatapp.ChatApplication
import com.hoangkotlin.chatapp.R
import com.hoangkotlin.chatapp.databinding.FragmentChatBinding
import com.hoangkotlin.chatapp.ui.home.adapter.MessageAdapter
import kotlinx.coroutines.launch


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (activity?.application as ChatApplication), lifecycleScope
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatHistoryLayout = LinearLayoutManager(requireContext())
        chatHistoryLayout.apply {
            stackFromEnd = true
            reverseLayout = false
        }

        val messageAdapter = MessageAdapter()
        binding.messageRecyclerView.apply {
            adapter = messageAdapter
            layoutManager = chatHistoryLayout
        }
        homeViewModel.currentChannel.observe(viewLifecycleOwner){
            activity?.title = it.name
        }

        messageAdapter.submitList(listOf())

        binding.send.setOnClickListener {
            if (binding.messageInput.text.isNotBlank()) {
                val message = binding.messageInput.text.toString().trim()
                sendMessage(message)
                binding.messageInput.setText("")
            }

        }
    }

    private fun sendMessage(message: String) {
        val senderUid = homeViewModel.currentDomainUser.uid
        homeViewModel.sendMessage(message)
    }
}