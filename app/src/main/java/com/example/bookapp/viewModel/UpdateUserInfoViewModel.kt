package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.repository.UpdateUserInfoRepository

class UpdateUserInfoViewModel(private val repository: UpdateUserInfoRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = repository.result()
    }
}