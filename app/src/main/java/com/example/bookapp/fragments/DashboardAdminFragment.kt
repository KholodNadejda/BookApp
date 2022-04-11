package com.example.bookapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.*
import com.example.bookapp.ViewModelFactory.CheckUserNameViewModelFactory
import com.example.bookapp.ViewModelFactory.DashboardViewModelFactory
import com.example.bookapp.ViewModelFactory.LogoutUserViewModelFactory
import com.example.bookapp.adapter.AdapterCategory
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentDashboardAdminBinding
import com.example.bookapp.repository.CheckUserRepositoryImpl
import com.example.bookapp.viewModel.*

class DashboardAdminFragment : Fragment() {

    private lateinit var binding: FragmentDashboardAdminBinding
    private lateinit var adapterCategory: AdapterCategory
    private lateinit var viewModel: DashboardViewModel
    private lateinit var checkUserNameViewModel: CheckUserNameViewModel
    private lateinit var logoutUserNameViewModel: LogoutUserViewModel
    private lateinit var dashboardRepositoryImpl: DashboardRepositoryImpl
    private lateinit var checkUserRepositoryImpl: CheckUserRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardAdminBinding.inflate(layoutInflater)
        dashboardRepositoryImpl = DashboardRepositoryImpl()
        viewModel = ViewModelProvider(this, DashboardViewModelFactory(dashboardRepositoryImpl))[DashboardViewModel::class.java]
        checkUserRepositoryImpl = CheckUserRepositoryImpl()
        checkUserNameViewModel = ViewModelProvider(this,
            CheckUserNameViewModelFactory(checkUserRepositoryImpl)
        )[CheckUserNameViewModel::class.java]
        logoutUserNameViewModel = ViewModelProvider(this, LogoutUserViewModelFactory(checkUserRepositoryImpl))[LogoutUserViewModel::class.java]

        checkUser()

        binding.addCategoryBtn.setOnClickListener {
            if(!MyApplication.hasConnection(requireActivity())){
                Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
            } else {
                navigator().showCategoryAddFragment()
            }
        }

        binding.addPdfFab.setOnClickListener {
            if(!MyApplication.hasConnection(requireActivity())){
                Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
            } else {
                navigator().showPdfAddFragment()

            }
        }

        binding.logOutBtn.setOnClickListener {
            logoutUserNameViewModel.modelsLiveData.observe(viewLifecycleOwner){
                if (it == true){
                    navigator().goToStart()
                }
            }
        }

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterCategory.filter.filter(p0)
                } catch (e: Exception) { }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        loadCategories()

        return binding.root
    }

    private fun loadCategories() {
        viewModel.modelsLiveData.observe(viewLifecycleOwner){
            adapterCategory = AdapterCategory(requireActivity(), it)
            binding.categoriesRv.adapter = adapterCategory
        }
    }

    private fun checkUser() {
       checkUserNameViewModel.modelsLiveData.observe(viewLifecycleOwner){
           binding.subTitleTv.text = it
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DashboardAdminFragment()
    }
}