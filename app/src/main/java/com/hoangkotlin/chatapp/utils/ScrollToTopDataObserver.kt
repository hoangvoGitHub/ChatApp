package com.hoangkotlin.chatapp.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollToTopDataObserver(
    private val layoutManager: LinearLayoutManager,
    private val recyclerView: RecyclerView)
    : RecyclerView.AdapterDataObserver() {
    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()

        // If the recycler view is initially being loaded or the user is at the bottom of the
        // list, scroll to the bottom of the list to show the newly added message.
        if (lastVisiblePosition == -1 || positionStart >= itemCount - 1 && lastVisiblePosition == positionStart - 1) {
            recyclerView.scrollToPosition(positionStart)
        }
    }
}