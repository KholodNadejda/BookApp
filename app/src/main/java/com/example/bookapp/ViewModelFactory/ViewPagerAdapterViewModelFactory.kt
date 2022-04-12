package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.ViewPagerAdapterRepository
import com.example.bookapp.viewModel.ViewPagerAdapterViewModel

class ViewPagerAdapterViewModelFactory(private val viewPagerAdapterRepository: ViewPagerAdapterRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewPagerAdapterViewModel::class.java)) {
            return ViewPagerAdapterViewModel(viewPagerAdapterRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}