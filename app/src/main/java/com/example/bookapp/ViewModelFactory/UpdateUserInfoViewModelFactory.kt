package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.UpdateUserInfoRepository
import com.example.bookapp.viewModel.UpdateUserInfoViewModel

class UpdateUserInfoViewModelFactory(private val updateUserInfoRepository: UpdateUserInfoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateUserInfoViewModel::class.java)) {
            return UpdateUserInfoViewModel(updateUserInfoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}