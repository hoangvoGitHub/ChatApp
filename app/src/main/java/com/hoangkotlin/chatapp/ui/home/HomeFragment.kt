package com.hoangkotlin.chatapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.hoangkotlin.chatapp.ChatApplication
import com.hoangkotlin.chatapp.R
import com.hoangkotlin.chatapp.data.model.User
import com.hoangkotlin.chatapp.databinding.FragmentHomeBinding
import com.hoangkotlin.chatapp.firebase.FirebaseService
import com.hoangkotlin.chatapp.ui.home.adapter.UserAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (activity?.application as ChatApplication), lifecycleScope
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slidingPaneLayout = binding.slidingPaneLayout

        val userAdapter = UserAdapter {
            homeViewModel.updateChatFriend(it)
            slidingPaneLayout.openPane()
        }



        binding.userRecyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        lifecycle.coroutineScope.launch {
            homeViewModel.allUser().collect() {
                userAdapter.submitList(it)
            }
        }

        // Connect the SlidingPaneLayout to the system back button.
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            FriendsListOnBackPressedCallback(slidingPaneLayout, activity)
        )

    }

    class FriendsListOnBackPressedCallback(
        private val slidingPaneLayout: SlidingPaneLayout,
        private val activity: FragmentActivity?
    ) : OnBackPressedCallback(slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen),
        SlidingPaneLayout.PanelSlideListener {

        init {
            slidingPaneLayout.addPanelSlideListener(this)
        }

        override fun handleOnBackPressed() {
            slidingPaneLayout.closePane()
            val auth = FirebaseAuth.getInstance()
            val name = auth.currentUser?.displayName?.split(" ")?.get(0)
            val title = "Hi! $name"
            activity?.title = title
        }

        override fun onPanelSlide(panel: View, slideOffset: Float) {
        }

        override fun onPanelOpened(panel: View) {
            isEnabled = true
        }

        override fun onPanelClosed(panel: View) {
            isEnabled = false
        }
    }


}
