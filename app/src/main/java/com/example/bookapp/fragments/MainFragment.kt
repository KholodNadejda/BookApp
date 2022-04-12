package com.example.bookapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentMainBinding
import com.example.bookapp.repository.CheckUserRepositoryImpl
import com.example.bookapp.viewModel.CheckUserViewModel
import com.example.bookapp.ViewModelFactory.CheckUserViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var checkUserViewModel: CheckUserViewModel
    private lateinit var checkUserRepositoryImpl: CheckUserRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser !== null) {
            Log.d("TAG11", "checkUser onCreateView true")
        } else {
            Log.d("TAG11", "checkUser onCreateView false")
        }
        Log.d("TAG11", "checkUser onCreateView")
        checkUser()

        binding.loginBtn.setOnClickListener {
            navigator().showLoginFragment()
        }
        binding.skipBtn.setOnClickListener {
            navigator().showDashboardUserFragment()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("TAG11", "checkUser onViewCreated")
        checkUser()
    }

    private fun checkUser() {
        checkUserRepositoryImpl = CheckUserRepositoryImpl()
        checkUserViewModel = ViewModelProvider(this,
            CheckUserViewModelFactory(checkUserRepositoryImpl)
        )[CheckUserViewModel::class.java]

        Log.d("TAG11", "checkUser ")
        checkUserViewModel.modelsLiveData.observe(viewLifecycleOwner){
            Log.d("TAG11", "checkUser it $it")
            if (it == "user") {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                navigator().showDashboardUserFragment()
            } else if (it == "admin") {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                navigator().showDashboardAdminFragment()
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}