package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.DashboardRepository
import com.example.bookapp.viewModel.DashboardViewModel

class DashboardViewModelFactory(
        private val DashboardRepository: DashboardRepository
) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(DashboardRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}