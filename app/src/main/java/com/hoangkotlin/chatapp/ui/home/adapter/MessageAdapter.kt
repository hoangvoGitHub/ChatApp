package com.hoangkotlin.chatapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.hoangkotlin.chatapp.data.model.ChatMessage
import com.hoangkotlin.chatapp.databinding.ReceivedMessageItemBinding
import com.hoangkotlin.chatapp.databinding.SentMessageItemBinding
import com.hoangkotlin.chatapp.utils.MessageViewType

class MessageAdapter : ListAdapter<ChatMessage, MessageAdapter.MessageViewHolder>(DiffCallback) {
    private lateinit var context: Context

    class MessageViewHolder(private var binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(Message: ChatMessage) {
            if (itemViewType == MessageViewType.SENT_MESSAGE) {
                (binding as SentMessageItemBinding).message.text = Message.content
            } else {
                (binding as ReceivedMessageItemBinding).message.text = Message.content
            }
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        context = parent.context

        return when (viewType) {
            MessageViewType.SENT_MESSAGE -> {
                val sentView = SentMessageItemBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                MessageViewHolder(sentView)
            }
            MessageViewType.RECEIVED_MESSAGE -> {
                val sentView = ReceivedMessageItemBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                MessageViewHolder(sentView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentItem = getItem(position)
        return when (currentItem.uid) {
            currentUser!!.uid -> MessageViewType.SENT_MESSAGE
            else -> MessageViewType.RECEIVED_MESSAGE
        }

    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = getItem(position)
        holder.bind(currentMessage)
    }
}