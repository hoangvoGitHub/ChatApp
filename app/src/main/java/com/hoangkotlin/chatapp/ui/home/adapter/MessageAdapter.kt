package com.hoangkotlin.chatapp.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.transition.TransitionManager
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.hoangkotlin.chatapp.R
import com.hoangkotlin.chatapp.databinding.ReceivedMessageItemBinding
import com.hoangkotlin.chatapp.databinding.SentMessageItemBinding
import com.hoangkotlin.chatapp.testdata.message.ChatMessage
import com.hoangkotlin.chatapp.utils.MessageViewType
import com.hoangkotlin.chatapp.utils.SyncStatus
import io.getstream.avatarview.coil.loadImage

class MessageAdapter : ListAdapter<ChatMessage, MessageAdapter.MessageViewHolder>(DiffCallback) {
    private lateinit var context: Context
    private lateinit var parent: ViewGroup
    private var previousExpandedPosition = -1
    private var mExpandedPosition = -1;

    class MessageViewHolder(var binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(Message: ChatMessage) {
            if (itemViewType == MessageViewType.SENT_MESSAGE) {
                (binding as SentMessageItemBinding).message.text = Message.content
                (binding as SentMessageItemBinding).textDateTime.text = Message.createdAt.toString()
                (binding as SentMessageItemBinding).messageStatus.setBackgroundResource(
                    chooseMessageStatusIcon(Message.syncStatus)
                )

            } else {
                (binding as ReceivedMessageItemBinding).message.text = Message.content
                (binding as ReceivedMessageItemBinding).avatarImage.loadImage(R.drawable.avatar)

            }
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.id == newItem.id && oldItem.syncStatus == newItem.syncStatus
        }

        override fun getChangePayload(oldItem: ChatMessage, newItem: ChatMessage): Any {
            return if (oldItem.syncStatus != newItem.syncStatus) {
                newItem.syncStatus
            } else {
                oldItem.syncStatus
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        context = parent.context
        this@MessageAdapter.parent = parent
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


    override fun onBindViewHolder(holder: MessageViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentMessage = getItem(position)
        val isExpanded = position == mExpandedPosition
        chooseMessageDetailBehavior(holder.binding, position, if (isExpanded) View.VISIBLE else View.GONE)
        holder.itemView.isActivated = isExpanded

        if (isExpanded){
            previousExpandedPosition = position
        }
        holder.itemView.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            notifyItemChanged(previousExpandedPosition);
            notifyItemChanged(position);

        }
        holder.bind(currentMessage)
    }

     private fun chooseMessageDetailBehavior(binding: ViewBinding, position: Int, behavior: Int) {
        if (getItemViewType(position) == MessageViewType.SENT_MESSAGE) {
            (binding as SentMessageItemBinding).textDateTime.visibility = behavior
        } else {
            (binding as ReceivedMessageItemBinding).textDateTime.visibility = behavior
        }
    }
}
fun chooseMessageStatusIcon(syncStatus: SyncStatus): Int = when (syncStatus) {
    SyncStatus.IN_PROGRESS -> R.drawable.pending_icon
    SyncStatus.COMPLETED -> R.drawable.sent_icon
    else -> R.drawable.received_icon
}
