package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.repository.LoadBookDetailsRepository

class LoadBookDetailsViewModel(private val repository: LoadBookDetailsRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<ArrayList<String>>()
    init {
        modelsLiveData = repository.loadBookDetails()
    }
}