package com.hoangkotlin.chatapp.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hoangkotlin.chatapp.ChatApplication
import com.hoangkotlin.chatapp.ui.MainActivity
import com.hoangkotlin.chatapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val profileViewModel: ProfileViewModel by activityViewModels {
        ProfileViewModelFactory(
            (activity?.application as ChatApplication)
        )
    }
//        ViewModelProvider(this)[ProfileViewModel::class.java]

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hostActivity = requireActivity() as MainActivity

        hostActivity.currentUser?.let {
            Log.d("Profile Fragment", "Current User ${it.displayName}")
            profileViewModel.getUser(it.uid)
        }
        val textView: TextView = binding.textGallery
        profileViewModel.user.observe(viewLifecycleOwner){
            textView.text = it.name
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}