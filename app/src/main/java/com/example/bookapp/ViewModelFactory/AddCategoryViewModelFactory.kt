package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.AddCategoryRepository
import com.example.bookapp.viewModel.AddCategoryViewModel

class AddCategoryViewModelFactory(private val AddCategoryRepository: AddCategoryRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCategoryViewModel::class.java)) {
            return AddCategoryViewModel(AddCategoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}