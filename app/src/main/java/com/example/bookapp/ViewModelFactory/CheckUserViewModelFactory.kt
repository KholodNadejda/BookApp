package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.repository.CheckUserRepository
import com.example.bookapp.viewModel.CheckUserNameViewModel
import com.example.bookapp.viewModel.CheckUserViewModel
import com.example.bookapp.viewModel.LogoutUserViewModel

class CheckUserViewModelFactory(private val CheckUserRepository: CheckUserRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckUserViewModel::class.java)) {
            return CheckUserViewModel(CheckUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CheckUserNameViewModelFactory(private val CheckUserRepository: CheckUserRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckUserNameViewModel::class.java)) {
            return CheckUserNameViewModel(CheckUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LogoutUserViewModelFactory(private val CheckUserRepository: CheckUserRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogoutUserViewModel::class.java)) {
            return LogoutUserViewModel(CheckUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}