package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.LoginUserRepository
import com.example.bookapp.viewModel.LoginUserViewModel

class LoginUserViewModelFactory(private val LoginUserRepository: LoginUserRepository):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginUserViewModel::class.java)) {
            return LoginUserViewModel(LoginUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}