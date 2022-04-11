package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.model.ModelPdf
import com.example.bookapp.repository.RecoverPasswordRepository

class RecoverPasswordViewModel(private var repository: RecoverPasswordRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = repository.getResult()
    }
}