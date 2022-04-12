package com.example.bookapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.*
import com.example.bookapp.viewModel.PdfListViewModel
import com.example.bookapp.ViewModelFactory.PdfListViewModelFactory
import com.example.bookapp.adapter.AdapterPdfAdmin
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentPdfListAdminBinding
import java.lang.Exception

class PdfListAdminFragment : Fragment() {

    private lateinit var binding: FragmentPdfListAdminBinding

    private var categoryId = ""
    private var category = ""
    private lateinit var adapterPdfAdmin: AdapterPdfAdmin
    private lateinit var viewModel: PdfListViewModel
    private lateinit var pdfListRepositoryImpl: PdfListRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPdfListAdminBinding.inflate(layoutInflater)

        categoryId = this.requireArguments().getString("categoryID").toString()
        category = this.requireArguments().getString("category").toString()
        binding.subTitleTv.text = category

        pdfListRepositoryImpl = PdfListRepositoryImpl(categoryId)
        viewModel = ViewModelProvider(this, PdfListViewModelFactory(pdfListRepositoryImpl))[PdfListViewModel::class.java]

        loadPdfList()

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterPdfAdmin.filter!!.filter(p0)
                } catch (e: Exception) {
                    Log.d(TAG, "onTextChange: ${e.message}")
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.backBtn.setOnClickListener {
            navigator().goBack()
        }

        return binding.root

    }

    private fun loadPdfList() {

        viewModel.modelsLiveData.observe(viewLifecycleOwner){
            adapterPdfAdmin = AdapterPdfAdmin(requireActivity(), it)
            binding.booksRv.adapter = adapterPdfAdmin
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PdfListAdminFragment()
        const val TAG = "PDF_LIST_ADMIN_TAG"
    }
}