package com.example.bookapp.fragments

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.MyApplication
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentPdfDetailBinding
import com.example.bookapp.repository.DownloadBookRepositoryImpl
import com.example.bookapp.repository.LoadBookDetailsRepositoryImpl
import com.example.bookapp.viewModel.DownloadBookViewModel
import com.example.bookapp.ViewModelFactory.DownloadBookViewModelFactory
import com.example.bookapp.viewModel.LoadBookDetailsViewModel
import com.example.bookapp.ViewModelFactory.LoadBookDetailsViewModelFactory

class PdfDetailFragment : Fragment() {

    private lateinit var binding: FragmentPdfDetailBinding
    private lateinit var downloadBookRepositoryImpl: DownloadBookRepositoryImpl
    private lateinit var downloadBookViewModel: DownloadBookViewModel
    private lateinit var loadBookDetailsRepositoryImpl: LoadBookDetailsRepositoryImpl
    private lateinit var loadBookDetailsViewModel: LoadBookDetailsViewModel
    private var bookId = ""
    private var bookUrl = ""

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPdfDetailBinding.inflate(layoutInflater)

        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        bookId = this.requireArguments().getString("bookId").toString()

        binding.backBtn.setOnClickListener {
            navigator().goBack()
        }
        binding.downloadBookBtn.setOnClickListener {
            if(!MyApplication.hasConnection(requireActivity())){
                Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
            } else {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "onCreate: STORAGE PERMISSION is already granted")
                    downloadBook()
                } else {
                    Log.d(TAG, "onCreate: STORAGE PERMISSION was not granted, LETS request it")
                    requestStoragePermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }

        loadBookDetails()

        return binding.root
    }

    private val requestStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d(TAG, "onCreate: STORAGE PERMISSION is granted")
                downloadBook()
            } else {
                Log.d(TAG, "onCreate: STORAGE PERMISSION is denied")
                Toast.makeText(requireActivity(), "PERMISSION denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun downloadBook() {
        progressDialog.setMessage("Downloading Book")
        progressDialog.show()

        downloadBookRepositoryImpl = DownloadBookRepositoryImpl(bookUrl, bookId)
        downloadBookViewModel = ViewModelProvider(this, DownloadBookViewModelFactory(downloadBookRepositoryImpl)
        )[DownloadBookViewModel::class.java]

        downloadBookViewModel.modelsLiveData.observe(viewLifecycleOwner){
            progressDialog.dismiss()
            Toast.makeText(
                requireActivity(),
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadBookDetails() {
        loadBookDetailsRepositoryImpl = LoadBookDetailsRepositoryImpl(bookId, binding.categoryTv, binding.sizeTv)
        loadBookDetailsViewModel = ViewModelProvider(this, LoadBookDetailsViewModelFactory(loadBookDetailsRepositoryImpl)
        )[LoadBookDetailsViewModel::class.java]

        loadBookDetailsViewModel.modelsLiveData.observe(viewLifecycleOwner){
            binding.titleTv.text = it[0]
            binding.descriptionTv.text = it[1]
            binding.viewsTv.text = it[2]
            binding.downloadsTv.text = it[3]
            binding.dateTv.text = it[4]
            bookUrl = it[5]
        }
    }

    companion object {
        const val TAG = "BOOK_DETAILS_TAG"

        @JvmStatic
        fun newInstance() = PdfDetailFragment()
    }
}