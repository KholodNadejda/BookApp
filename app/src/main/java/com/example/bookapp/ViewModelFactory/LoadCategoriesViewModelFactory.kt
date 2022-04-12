package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.LoadCategoriesRepository
import com.example.bookapp.viewModel.LoadCategoriesIdViewModel
import com.example.bookapp.viewModel.LoadCategoriesTitleViewModel

class LoadCategoriesIdViewModelFactory(private val loadCategoriesRepository: LoadCategoriesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadCategoriesIdViewModel::class.java)) {
            return LoadCategoriesIdViewModel(loadCategoriesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LoadCategoriesTitleViewModelFactory(private val loadCategoriesRepository: LoadCategoriesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadCategoriesTitleViewModel::class.java)) {
            return LoadCategoriesTitleViewModel(loadCategoriesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}