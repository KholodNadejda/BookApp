package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.DownloadBookRepository
import com.example.bookapp.viewModel.DownloadBookViewModel

class DownloadBookViewModelFactory(private val downloadBookRepository: DownloadBookRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DownloadBookViewModel::class.java)) {
            return DownloadBookViewModel(downloadBookRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}