package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.DeleteCategoryRepository
import com.example.bookapp.viewModel.DeleteCategoryViewModel

class DeleteCategoryViewModelFactory(private val DeleteCategoryRepository: DeleteCategoryRepository) :
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteCategoryViewModel::class.java)) {
            return DeleteCategoryViewModel(DeleteCategoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}