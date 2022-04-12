package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.RecoverPasswordRepository
import com.example.bookapp.viewModel.RecoverPasswordViewModel

class RecoverPasswordViewModelFactory(private val recoverPasswordRepository: RecoverPasswordRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecoverPasswordViewModel::class.java)) {
            return RecoverPasswordViewModel(recoverPasswordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}