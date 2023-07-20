package com.hoangkotlin.chatapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.hoangkotlin.chatapp.ChatApplication
import com.hoangkotlin.chatapp.ui.MainActivity
import com.hoangkotlin.chatapp.databinding.FragmentHomeBinding
import com.hoangkotlin.chatapp.data.local.user.User
import com.hoangkotlin.chatapp.ui.home.adapter.UserAdapter
import com.hoangkotlin.chatapp.utils.asUser
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (activity?.application as ChatApplication), lifecycleScope
        )
    }

    private var backPressedTime: Long = 0
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

        val hostActivity = requireActivity() as MainActivity

        hostActivity.currentUser?.let {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(it.username, it.password)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Network Error, Cannot log in!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            homeViewModel.setCurrentUser(it.asUser())
        }


        val userAdapter = UserAdapter {
            goToChatScreen(it)
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


        val callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                if (backPressedTime + 3000 > System.currentTimeMillis()) {
                    hostActivity.finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Press back again to leave the app.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                backPressedTime = System.currentTimeMillis()
            }

        }

        hostActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
    }

    private fun goToChatScreen(chatUser: User) {
        homeViewModel.updateChatFriend(chatUser)
        val action = HomeFragmentDirections.actionNavHomeToChatFragment(chatUser.name)
        findNavController().navigate(action)
    }


}
