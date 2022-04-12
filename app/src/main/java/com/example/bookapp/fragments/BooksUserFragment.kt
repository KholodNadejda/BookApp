package com.example.bookapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.*
import com.example.bookapp.ViewModelFactory.PdfListAllViewModelFactory
import com.example.bookapp.ViewModelFactory.PdfListMostDownloadedViewModelFactory
import com.example.bookapp.ViewModelFactory.PdfListViewModelFactory
import com.example.bookapp.adapter.AdapterPdfUser
import com.example.bookapp.databinding.FragmentBooksUserBinding
import com.example.bookapp.viewModel.*
import java.lang.Exception

class BooksUserFragment : Fragment {

    private lateinit var binding: FragmentBooksUserBinding

    private var categoryId = ""
    private var category = ""
    private var uid = ""

    private lateinit var adapterPdfUser: AdapterPdfUser

    private lateinit var viewModel: PdfListViewModel
    private lateinit var viewModelMD: PdfListMostDownloadedViewModel
    private lateinit var viewModelAll: PdfListAllViewModel


    constructor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        if (args != null) {
            categoryId = args.getString("categoryId")!!
            category = args.getString("category")!!
            uid = args.getString("uid")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksUserBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(this, PdfListViewModelFactory(PdfListRepositoryImpl(categoryId)))[PdfListViewModel::class.java]
        viewModelAll = ViewModelProvider(this, PdfListAllViewModelFactory(PdfListRepositoryImpl(categoryId)))[PdfListAllViewModel::class.java]
        viewModelMD = ViewModelProvider(this, PdfListMostDownloadedViewModelFactory(PdfListRepositoryImpl(categoryId)))[PdfListMostDownloadedViewModel::class.java]


        when (category) {
            "All" -> {
                loadAllBooks()
            }
            "Most Downloaded" -> {
                loadMostDownloadedBooks()
            }
            "MOST DOWNLOADED" -> {
                loadMostDownloadedBooks()
            }
            else -> {
                loadCategoriesBooks()
            }
        }

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterPdfUser.filter.filter(p0)
                } catch (e: Exception) {
                    Log.d(PdfListAdminFragment.TAG, "onTextChange: ${e.message}")
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        return binding.root
    }

    private fun loadAllBooks() {
        viewModelAll.modelsLiveData.observe(viewLifecycleOwner){
            adapterPdfUser = AdapterPdfUser(requireActivity(), it)
            binding.booksRv.adapter = adapterPdfUser
        }
    }

    private fun loadMostDownloadedBooks() {
        Log.d(PdfDetailFragment.TAG, "onDataChange most result ")
        viewModelMD.modelsLiveData.observe(viewLifecycleOwner){
            adapterPdfUser = AdapterPdfUser(requireActivity(), it)
            Log.d(PdfDetailFragment.TAG, "onDataChange most result:${it[0].downloadCount} ${it[1].downloadCount} ${it[2].downloadCount}")
            binding.booksRv.adapter = adapterPdfUser
        }
    }

    private fun loadCategoriesBooks() {

        viewModel.modelsLiveData.observe(viewLifecycleOwner){
            adapterPdfUser = AdapterPdfUser(requireActivity(), it)
            binding.booksRv.adapter = adapterPdfUser
        }
    }

    companion object {
        fun newInstance(categoryId: String, category: String, uid: String): BooksUserFragment {
            val fragment = BooksUserFragment()
            val args = Bundle()
            args.putString("categoryId", categoryId)
            args.putString("category", category)
            args.putString("uid", uid)
            fragment.arguments = args
            return fragment
        }
    }
}