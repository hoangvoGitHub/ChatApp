package com.hoangkotlin.chatapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.hoangkotlin.chatapp.ChatApplication
import com.hoangkotlin.chatapp.R
import com.hoangkotlin.chatapp.databinding.FragmentChatBinding
import com.hoangkotlin.chatapp.ui.home.adapter.MessageAdapter
import com.hoangkotlin.chatapp.utils.MyScrollToBottomObserver
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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val messageInput = binding.messageInput
        val messageAdapter = MessageAdapter()
        val manager = LinearLayoutManager(requireContext())
        manager.stackFromEnd = true

        binding.messageRecyclerView.apply {
            adapter = messageAdapter
            layoutManager = manager

        }

        messageAdapter.registerAdapterDataObserver(
            MyScrollToBottomObserver(
                binding.messageRecyclerView,
                messageAdapter,
                manager
            )
        )

        homeViewModel.currentChannel.observe(viewLifecycleOwner) {
            activity?.title = it.name
        }

        homeViewModel.currentChannel.observe(viewLifecycleOwner) {
            lifecycle.coroutineScope.launch {
                homeViewModel.currentMessageHistory().collect() {
                    binding.messageRecyclerView.scrollToPosition(it.size - 1)
                    messageAdapter.submitList(it)
                }
            }
        }

        messageInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.galleryButton.visibility = View.GONE
                binding.cameraButton.visibility = View.GONE
                binding.voiceButton.visibility = View.GONE
                binding.moreButton.load(R.drawable.ic_more)
            } else {
                binding.galleryButton.visibility = View.VISIBLE
                binding.cameraButton.visibility = View.VISIBLE
                binding.voiceButton.visibility = View.VISIBLE
                binding.moreButton.load(R.drawable.ic_more_dot)
            }
        }


        messageInput.setOnClickListener {
            binding.galleryButton.visibility = View.GONE
            binding.cameraButton.visibility = View.GONE
            binding.voiceButton.visibility = View.GONE
            binding.moreButton.load(R.drawable.ic_more)
        }

        binding.moreButton.setOnClickListener {
            if (binding.galleryButton.visibility == View.GONE) {
                binding.galleryButton.visibility = View.VISIBLE
                binding.cameraButton.visibility = View.VISIBLE
                binding.voiceButton.visibility = View.VISIBLE
                binding.moreButton.load(R.drawable.ic_more_dot)
            }
        }

        binding.send.setOnClickListener {
            if (messageInput.text.isNotBlank()) {
                val message = messageInput.text.toString().trim()
                sendMessage(message)
                messageInput.setText("")
            }
        }
    }

    private fun sendMessage(message: String) {
        homeViewModel.sendMessage(message)
    }
}