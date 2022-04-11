package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.repository.UploadPdfToStorageRepository

class UploadPdfToStorageViewModel(private var repository: UploadPdfToStorageRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = repository.uploadPdfToStorage()
    }
}