package com.hoangkotlin.chatapp.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoangkotlin.chatapp.ChatApplication
import com.hoangkotlin.chatapp.R
import com.hoangkotlin.chatapp.databinding.FragmentSearchBinding
import com.hoangkotlin.chatapp.ui.home.HomeViewModel
import com.hoangkotlin.chatapp.ui.home.HomeViewModelFactory
import com.hoangkotlin.chatapp.ui.home.adapter.UserAdapter
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {


    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (activity?.application as ChatApplication), lifecycleScope
        )
    }
    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userAdapter = UserAdapter {
        }

        binding.searchResult.apply {
            adapter = userAdapter
        }


        lifecycle.coroutineScope.launch {
            homeViewModel.allUser().collect(){
                userAdapter.submitList(it)
            }
        }

    }

}