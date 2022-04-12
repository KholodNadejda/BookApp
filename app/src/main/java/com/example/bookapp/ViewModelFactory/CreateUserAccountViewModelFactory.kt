package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.CreateUserAccountRepository
import com.example.bookapp.viewModel.CreateUserAccountViewModel

class CreateUserAccountViewModelFactory(private val createUserAccountRepository: CreateUserAccountRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateUserAccountViewModel::class.java)) {
            return CreateUserAccountViewModel(createUserAccountRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}