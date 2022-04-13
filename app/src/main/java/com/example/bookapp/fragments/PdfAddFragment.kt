package com.example.bookapp.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.DashboardRepositoryImpl
import com.example.bookapp.MyApplication
import com.example.bookapp.model.ModelCategory
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentPdfAddBinding
import com.example.bookapp.repository.UploadPdfToStorageRepositoryImpl
import com.example.bookapp.viewModel.DashboardViewModel
import com.example.bookapp.ViewModelFactory.DashboardViewModelFactory
import com.example.bookapp.viewModel.UploadPdfToStorageViewModel
import com.example.bookapp.ViewModelFactory.UploadPdfToStorageViewModelFactory

class PdfAddFragment : Fragment() {

    private lateinit var binding: FragmentPdfAddBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var categoryArrayList: List<ModelCategory>
    private lateinit var viewModel: DashboardViewModel
    private lateinit var dashboardRepositoryImpl: DashboardRepositoryImpl
    private lateinit var uploadPdfToStorageViewModel: UploadPdfToStorageViewModel
    private lateinit var uploadPdfToStorageRepositoryImpl: UploadPdfToStorageRepositoryImpl
    private var pdfUri: Uri? = null
    private var selectedCategoryId = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPdfAddBinding.inflate(layoutInflater)

        dashboardRepositoryImpl = DashboardRepositoryImpl()
        viewModel = ViewModelProvider(this, DashboardViewModelFactory(dashboardRepositoryImpl))[DashboardViewModel::class.java]

        loadPdfCategories()

        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.categoryTv.setOnClickListener {
            categoryPickDialog()
        }

        binding.attachPdfBtn.setOnClickListener {
            pdfPickIntent()
        }

        binding.submitBtn.setOnClickListener {
            if(!MyApplication.hasConnection(requireActivity())){
                Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
            } else {
                validateData()
            }
        }

        binding.backBtn.setOnClickListener {
            navigator().goBack()
        }

        return binding.root
    }

    private fun validateData() {
        Log.d("PDF_ADD_TAG", "validateData: validate Data")
        val title = binding.titleEt.text.toString().trim()
        val description = binding.descriptionEt.text.toString().trim()
        val category = binding.categoryTv.text.toString().trim()

        when {
            title.isEmpty() -> {
                Toast.makeText(requireActivity(), "Enter Title", Toast.LENGTH_SHORT).show()
            }
            description.isEmpty() -> {
                Toast.makeText(requireActivity(), "Enter Description", Toast.LENGTH_SHORT).show()
            }
            category.isEmpty() -> {
                Toast.makeText(requireActivity(), "Enter Category", Toast.LENGTH_SHORT).show()
            }
            pdfUri == null -> {
                Toast.makeText(requireActivity(), "Pick PDF", Toast.LENGTH_SHORT).show()
            }
            else -> {
                uploadPdfToStorage(title, description, category)
            }
        }
    }

    private fun uploadPdfToStorage(title: String, description: String, category: String) {
        Log.d("PDF_ADD_TAG", "uploadPdfToStorage: uploading To Storage")

        progressDialog.setMessage("Uploading PDF")
        progressDialog.show()

        uploadPdfToStorageRepositoryImpl = UploadPdfToStorageRepositoryImpl(pdfUri, title, description, category, selectedCategoryId)
        uploadPdfToStorageViewModel = ViewModelProvider(this, UploadPdfToStorageViewModelFactory(uploadPdfToStorageRepositoryImpl)
        )[UploadPdfToStorageViewModel::class.java]

        uploadPdfToStorageViewModel.modelsLiveData.observe(viewLifecycleOwner){
            progressDialog.dismiss()
            Toast.makeText(
                requireActivity(),
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadPdfCategories() {

        viewModel.modelsLiveData.observe(viewLifecycleOwner){
            categoryArrayList = it
        }
    }

    private fun categoryPickDialog() {
        Log.d("PDF_ADD_TAG", "categoryPickDialog: Showing pdf category pick dialog")
        val categoriesArray = arrayOfNulls<String>(categoryArrayList.size)
        for (i in categoryArrayList.indices) {
            categoriesArray[i] = categoryArrayList[i].category
        }
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Pick Category")
            .setItems(categoriesArray) { dialog, which ->
                val selectedCategoryTitle = categoryArrayList[which].category
                selectedCategoryId = categoryArrayList[which].id
                binding.categoryTv.text = selectedCategoryTitle

                Log.d("PDF_ADD_TAG", "categoryPickDialog: Selected Category ID: $selectedCategoryId")
                Log.d("PDF_ADD_TAG", "categoryPickDialog: Selected Category Title: $selectedCategoryTitle")
            }
            .show()
    }

    private fun pdfPickIntent() {
        Log.d("PDF_ADD_TAG", "pdfPickIntent: start pdf Pick Intent")
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        pdfResultLauncher.launch(intent)
    }

    private val pdfResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("PDF_ADD_TAG", "PDF Picked ")
                pdfUri = result.data!!.data
            } else {
                Log.d("PDF_ADD_TAG", "PDF Pick cancelled ")
                Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )

    companion object {
        @JvmStatic
        fun newInstance() = PdfAddFragment()
    }
}