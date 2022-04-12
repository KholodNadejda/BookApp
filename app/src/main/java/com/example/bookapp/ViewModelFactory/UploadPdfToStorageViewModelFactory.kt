package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.UploadPdfToStorageRepository
import com.example.bookapp.viewModel.UploadPdfToStorageViewModel

class UploadPdfToStorageViewModelFactory(private val uploadPdfToStorageRepository: UploadPdfToStorageRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadPdfToStorageViewModel::class.java)) {
            return UploadPdfToStorageViewModel(uploadPdfToStorageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}