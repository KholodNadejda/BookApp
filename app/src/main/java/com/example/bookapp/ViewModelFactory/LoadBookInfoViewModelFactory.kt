package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.LoadBookInfoRepository
import com.example.bookapp.viewModel.LoadBookInfoCategoryViewModel
import com.example.bookapp.viewModel.LoadBookInfoDescriptionViewModel
import com.example.bookapp.viewModel.LoadBookInfoTitleViewModel

class LoadBookInfoTitleViewModelFactory(private val loadBookInfoRepository: LoadBookInfoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadBookInfoTitleViewModel::class.java)) {
            return LoadBookInfoTitleViewModel(loadBookInfoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LoadBookInfoDescriptionViewModelFactory(private val loadBookInfoRepository: LoadBookInfoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadBookInfoDescriptionViewModel::class.java)) {
            return LoadBookInfoDescriptionViewModel(loadBookInfoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LoadBookInfoCategoryViewModelFactory(private val loadBookInfoRepository: LoadBookInfoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadBookInfoCategoryViewModel::class.java)) {
            return LoadBookInfoCategoryViewModel(loadBookInfoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}