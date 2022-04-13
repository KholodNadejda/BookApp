package com.example.bookapp.fragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.AddCategoryRepositoryImpl
import com.example.bookapp.viewModel.AddCategoryViewModel
import com.example.bookapp.ViewModelFactory.AddCategoryViewModelFactory
import com.example.bookapp.MyApplication
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentCategoryAddBinding

class CategoryAddFragment : Fragment() {

    private lateinit var binding: FragmentCategoryAddBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var viewModel: AddCategoryViewModel
    private lateinit var addCategoryRepositoryImpl: AddCategoryRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryAddBinding.inflate(layoutInflater)

        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.backBtn.setOnClickListener {
            navigator().goBack()
        }

        binding.submitBtn.setOnClickListener {
            if(!MyApplication.hasConnection(requireActivity())){
                Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
            } else {
                validateData()
            }
        }
        return binding.root
    }

    private fun validateData() {
        val category = binding.categoryEt.text.toString().trim()
        if (category.isEmpty()) {
            Toast.makeText(requireActivity(), "Enter Category", Toast.LENGTH_SHORT).show()
        } else {
            addCategoryFirebase(category)
        }
    }

    private fun addCategoryFirebase(category: String) {
        addCategoryRepositoryImpl = AddCategoryRepositoryImpl(category)
        viewModel = ViewModelProvider(this, AddCategoryViewModelFactory(addCategoryRepositoryImpl))[AddCategoryViewModel::class.java]
        progressDialog.show()
        viewModel.modelsLiveData.observe(viewLifecycleOwner){
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }
        progressDialog.dismiss()
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoryAddFragment()
    }
}