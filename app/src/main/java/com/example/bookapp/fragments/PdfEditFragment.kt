package com.example.bookapp.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.MyApplication
import com.example.bookapp.ViewModelFactory.*
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentPdfEditBinding
import com.example.bookapp.repository.UpdatePdfRepositoryImpl
import com.example.bookapp.repository.LoadBookInfoRepositoryImpl
import com.example.bookapp.repository.LoadCategoriesRepositoryImpl
import com.example.bookapp.viewModel.*

class PdfEditFragment : Fragment() {

    private lateinit var binding: FragmentPdfEditBinding

    private var selectCategoryId = ""

    private lateinit var categoryTitleArrayList: ArrayList<String>
    private lateinit var categoryIdArrayList: ArrayList<String>
    private lateinit var updatePdfViewModel: UpdatePdfViewModel
    private lateinit var updatePdfRepositoryImpl: UpdatePdfRepositoryImpl

    private lateinit var loadCategoriesRepositoryImpl: LoadCategoriesRepositoryImpl
    private lateinit var loadCategoriesIdViewModel: LoadCategoriesIdViewModel
    private lateinit var loadCategoriesTitleViewModel: LoadCategoriesTitleViewModel

    private lateinit var loadBookInfoCategoryViewModel: LoadBookInfoCategoryViewModel
    private lateinit var loadBookInfoDescriptionViewModel: LoadBookInfoDescriptionViewModel
    private lateinit var loadBookInfoTitleViewModel: LoadBookInfoTitleViewModel
    private lateinit var loadBookInfoRepositoryImpl: LoadBookInfoRepositoryImpl

    private lateinit var processDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPdfEditBinding.inflate(layoutInflater)

        val bookId = this.requireArguments().getString("bookId").toString()

        processDialog = ProgressDialog(requireActivity())
        processDialog.setTitle("Please wait")
        processDialog.setCanceledOnTouchOutside(false)

        binding.backBtn.setOnClickListener {
            navigator().goBack()
        }

        binding.categoryTv.setOnClickListener {
            categoryDialog()
        }

        binding.submitBtn.setOnClickListener {
            if(!MyApplication.hasConnection(requireActivity())){
                Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
            } else {
                validateData(bookId)
            }
        }

        loadCategories()
        loadBookInfo(bookId)

        return binding.root
    }

    private fun loadBookInfo(bookId: String) {
        Log.d(TAG, "loadBookInfo: Loading bool info")

        loadBookInfoRepositoryImpl = LoadBookInfoRepositoryImpl(bookId)

        loadBookInfoTitleViewModel = ViewModelProvider(this, LoadBookInfoTitleViewModelFactory(loadBookInfoRepositoryImpl)
        )[LoadBookInfoTitleViewModel::class.java]
        loadBookInfoDescriptionViewModel = ViewModelProvider(this, LoadBookInfoDescriptionViewModelFactory(loadBookInfoRepositoryImpl)
        )[LoadBookInfoDescriptionViewModel::class.java]
        loadBookInfoCategoryViewModel = ViewModelProvider(this, LoadBookInfoCategoryViewModelFactory(loadBookInfoRepositoryImpl)
        )[LoadBookInfoCategoryViewModel::class.java]

        loadBookInfoTitleViewModel.modelsLiveData.observe(viewLifecycleOwner){
            binding.titleEt.setText(it)
        }
        loadBookInfoDescriptionViewModel.modelsLiveData.observe(viewLifecycleOwner){
            binding.descriptionEt.setText(it)
        }
        loadBookInfoCategoryViewModel.modelsLiveData.observe(viewLifecycleOwner){
            binding.categoryTv.text = it
        }
    }

    private fun validateData(bookId: String) {
        val title = binding.titleEt.text.toString().trim()
        val description = binding.descriptionEt.text.toString().trim()

        when {
            title.isEmpty() -> {
                Toast.makeText(requireActivity(), "Enter title", Toast.LENGTH_SHORT).show()
            }
            description.isEmpty() -> {
                Toast.makeText(requireActivity(), "Enter description", Toast.LENGTH_SHORT).show()
            }
            selectCategoryId.isEmpty() -> {
                Toast.makeText(requireActivity(), "Pick Category", Toast.LENGTH_SHORT).show()
            }
            else -> {
                updatePdf(bookId, title, description)
            }
        }
    }

    private fun updatePdf(bookId: String, title: String, description: String) {
        processDialog.setMessage("Updating book info")
        processDialog.show()

        updatePdfRepositoryImpl = UpdatePdfRepositoryImpl(bookId, title, description, selectCategoryId)
        updatePdfViewModel = ViewModelProvider(this, UpdatePdfViewModelFactory(updatePdfRepositoryImpl)
        )[UpdatePdfViewModel::class.java]

        updatePdfViewModel.modelsLiveData.observe(viewLifecycleOwner){
            processDialog.dismiss()
            Toast.makeText(
                requireActivity(),
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun categoryDialog() {
        val categoriesArray = arrayOfNulls<String>(categoryTitleArrayList.size)
        for (i in categoryTitleArrayList.indices) {
            categoriesArray[i] = categoryTitleArrayList[i]
        }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Choose Category")
            .setItems(categoriesArray) { dialopg, position ->
                selectCategoryId = categoryIdArrayList[position]
                val selectCategoryTitle = categoryTitleArrayList[position]

                binding.categoryTv.text = selectCategoryTitle
            }
            .show()
    }

    private fun loadCategories() {
        categoryTitleArrayList = ArrayList()
        categoryIdArrayList = ArrayList()

        loadCategoriesRepositoryImpl = LoadCategoriesRepositoryImpl()
        loadCategoriesIdViewModel = ViewModelProvider(this, LoadCategoriesIdViewModelFactory(loadCategoriesRepositoryImpl)
        )[LoadCategoriesIdViewModel::class.java]
        loadCategoriesTitleViewModel = ViewModelProvider(this, LoadCategoriesTitleViewModelFactory(loadCategoriesRepositoryImpl)
        )[LoadCategoriesTitleViewModel::class.java]

        loadCategoriesIdViewModel.modelsLiveData.observe(viewLifecycleOwner){
            categoryIdArrayList = it
        }
        loadCategoriesTitleViewModel.modelsLiveData.observe(viewLifecycleOwner){
            categoryTitleArrayList = it
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = PdfEditFragment()
        const val TAG = "PDF_EDIT_TAG"
    }
}