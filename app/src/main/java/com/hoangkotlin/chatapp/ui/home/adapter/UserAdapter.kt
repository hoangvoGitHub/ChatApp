package com.hoangkotlin.chatapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hoangkotlin.chatapp.R
import com.hoangkotlin.chatapp.data.model.User
import com.hoangkotlin.chatapp.databinding.FriendViewItemBinding


class UserAdapter(private val onItemClicked: (User) -> Unit) :
    ListAdapter<User, UserAdapter.UsersViewHolder>(DiffCallback) {

    private lateinit var context: Context

    class UsersViewHolder(private var binding: FriendViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userName.text = user.name
            binding.avatarImage.load(R.drawable.avatar)
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        context = parent.context
        return UsersViewHolder(
            FriendViewItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val currentFriend = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(currentFriend)
        }
        holder.bind(currentFriend)
    }

}