package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.UpdatePdfRepository
import com.example.bookapp.viewModel.UpdatePdfViewModel

class UpdatePdfViewModelFactory(private val updatePdfRepository: UpdatePdfRepository):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdatePdfViewModel::class.java)) {
            return UpdatePdfViewModel(updatePdfRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}