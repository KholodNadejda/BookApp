package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.LoadBookDetailsRepository
import com.example.bookapp.viewModel.LoadBookDetailsViewModel

class LoadBookDetailsViewModelFactory(private val loadBookDetailsRepository: LoadBookDetailsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadBookDetailsViewModel::class.java)) {
            return LoadBookDetailsViewModel(loadBookDetailsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}