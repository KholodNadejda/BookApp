package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.repository.CreateUserAccountRepository

class CreateUserAccountViewModel(private val repository: CreateUserAccountRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = repository.result()
    }
}